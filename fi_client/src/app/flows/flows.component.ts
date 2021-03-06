import { forkJoin } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { SelectionModel } from '@angular/cdk/collections';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { INd, TresService } from '../tres.service';

export interface IFiModel {
  number: number;
  label: string;
  status: string;
}

@Component({
  selector: 'app-flows',
  templateUrl: './flows.component.html',
  styleUrls: ['./flows.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ]
})
export class FlowsComponent implements OnInit {
  @ViewChild('file') file;
  displayedColumns: string[] = ['select', 'number', 'label', 'status', 'download'];
  selection = new SelectionModel<any>(true, []);
  dataSource: any;
  fileTypes: any[] = [
    { value: 'yes_no', viewValue: 'yes_no' },
    { value: 'table', viewValue: 'table' }
  ];
  chooseUpload = false;
  canUpload = false;
  uploading = false;

  ndId: string;
  ndParentId: string;
  treId: string;
  fileType: string;
  public files: Set<File> = new Set();
  expandedElement: any;
  objectKeys = Object.keys;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private tresService: TresService,
              private http: HttpClient) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      if (this.file) {
        this.file.nativeElement.value = '';
      }
      this.fileType = undefined;
      this.canUpload = false;
      this.ndId = params.ndId;
      this.ndParentId = params.ndParentId;
      this.treId = params.treId;
      this.getFiList();
    });
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    if (!this.dataSource) return false;
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
    this.selection.clear() :
    this.dataSource.data.forEach(row => this.selection.select(row));
  }

  /** Selects upload new doc/docx file. */
  selectedUploadFile() {
    this.chooseUpload = true;
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: any): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.position + 1}`;
  }

  private getFiList() {
    if (this.ndId) {
      this.tresService.getNd(this.ndId).subscribe(
        (result) => {
          this.initDataSource(result);
        });
    }
  }

  private initDataSource(result) {
    const mapedResult = result.FI.map((fi, index) => {
      return {
        number: index + 1,
        label: fi.lbl,
        status: fi.status,
        ID: fi.ID,
        pgBounderies: fi.pgBounderies
      };
    });

    this.dataSource = new MatTableDataSource<any>(mapedResult);
  }

  upload() {
    if (this.file && this.file.nativeElement && this.file.nativeElement.files[0]) {
      this.uploading = true;
      this.tresService.addFi(this.ndId, this.fileType, this.file.nativeElement.files[0]).subscribe(
        (data: { message: string; }) => {
          this.getFiList();
          this.uploading = false;
          this.chooseUpload = false;
          this.file.nativeElement.value = '';
        },
        error => {
          this.uploading = false;
          this.chooseUpload = false;
          console.error(error);
          alert("Error occur:" + error);
          this.file.nativeElement.value = '';
        });
    }
  }

  fileTypeSelected($event: any) {
    this.canUpload = true;
    this.fileType = $event.value;
  }

  addFile() {
    this.file.nativeElement.click();
  }

  delete(fi: any) {
    this.tresService.deleteFi(fi.ID).subscribe(
      (data: INd) => {
        this.initDataSource(data);
      },
      error => {
        console.error(error);
        alert("Error occur:" + error);
      });
  }

  public download(fi: any): void {
    this.tresService.downloadFi(fi.ID).subscribe(x => {
      downloadFileByType(x, 'application/msword');
    });
  }

  exportSelected() {
    const selectedFIIds = this.selection.selected.map(row => {
      return { ID: row.ID};
    });
    const data = {
      ID: this.treId,
      ndParents: [
        {
          ID: this.ndParentId,
          ND: [
            {
              ID: this.ndId,
              FI: selectedFIIds
            }
          ]
        }
      ]
    };
    this.tresService.export(data).subscribe(
      (result: any) => {
        downloadFileByType(result, 'application/octet-stream', 'fis.zip');
      },
      error => {
        console.error(error);
        alert("Error occur:" + error);
      });
  }

  reportSelected() {
    const selectedFIIds = this.selection.selected.map(row => {
      return { ID: row.ID};
    });
    const data = {
      ID: this.treId,
      ndParents: [
        {
          ID: this.ndParentId,
          ND: [
            {
              ID: this.ndId,
              FI: selectedFIIds
            }
          ]
        }
      ]
    };
    this.tresService.report(data).subscribe(
      (result: any) => {
        downloadFileByType(result, 'application/octet-stream', 'report.pdf');
        // this.downloadFile(result, 'application/octet-stream');
      },
      error => {
        console.error(error);
        alert("Error occur:" + error);
      });
  }


  downloadFile(data: any, type: string) {
    const blob = new Blob([data], { type: type.toString() });
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url);
    if (!pwa || pwa.closed || pwa.closed === undefined) {
      alert('Please disable your Pop-up blocker and try again.');
    }
  }

  deleteSelected() {
    const selectedFIIds = this.selection.selected.map(row => {
      return { ID: row.ID};
    });
    if (confirm('Are you sure delete selected?')) {
      const deleteObsList = [];
      selectedFIIds.forEach(p => {
        deleteObsList.push(this.tresService.deleteFi(p.ID));
      });
      forkJoin(deleteObsList).subscribe(
        (data: INd[]) => {
          this.getFiList();
          // this.initDataSource(data[data.length - 1]);
          this.selection.clear();
        },
        error => {
          console.error(error);
          alert("Error occur:" + error);
        });
    }
  }
}

export function downloadFileByType(file: any, type: string, nameForDownload?: string) {
  const newBlob = new Blob([file], { type });

  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
    window.navigator.msSaveOrOpenBlob(newBlob);
    return;
  }

  // For other browsers:
  // Create a link pointing to the ObjectURL containing the blob.
  const data = window.URL.createObjectURL(newBlob);

  const link = document.createElement('a');
  link.href = data;
  link.download = nameForDownload ? nameForDownload : 'fiDoc.doc';
  // this is necessary as link.click() does not work on the latest firefox
  link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

  setTimeout(() => {
    // For Firefox it is necessary to delay revoking the ObjectURL
    window.URL.revokeObjectURL(data);
    link.remove();
  }, 100);
}
