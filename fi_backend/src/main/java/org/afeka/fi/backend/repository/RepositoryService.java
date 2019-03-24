package org.afeka.fi.backend.repository;

import org.afeka.fi.backend.common.FiCommon;
import org.afeka.fi.backend.common.Helpers;
import org.afeka.fi.backend.exception.DataFactoryNotFoundException;
import org.afeka.fi.backend.exception.ResourceNotFoundException;
import org.afeka.fi.backend.pojo.commonstructure.FI;
import org.afeka.fi.backend.pojo.commonstructure.ND;
import org.afeka.fi.backend.pojo.commonstructure.NdParent;
import org.afeka.fi.backend.pojo.commonstructure.TRE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService extends FiCommon {

    @Autowired
    FIRepository fiRepository;
    @Autowired
    NdRepository ndRepository;
    @Autowired
    NdParentRepository ndParentRepository;
    @Autowired
    TreRepository treRepository;


    public TRE save(TRE tre) {
        tre.ndParents.forEach(ndParent -> save(ndParent));
        return treRepository.save(tre);
    }

    public NdParent save(NdParent ndParent) {
        ndParent.ND.forEach(nd -> save(nd));
        return ndParentRepository.save(ndParent);
    }

    public ND save(ND nd){
            nd.FI.forEach(fi-> fiRepository.save(fi));
            return ndRepository.save(nd);
    }

    public FI add(FI fi){
        logger.called("add","fi",fi);
        return fiRepository.save(fi);
    }

    public ND add(ND nd){
        logger.called("add","nd",nd);
        return ndRepository.save(nd);
    }

    public NdParent add(NdParent ndParent){
        logger.called("add","ndParent",ndParent);
        return ndParentRepository.save(ndParent);
    }

    public TRE add(TRE tre){
        logger.called("add","tre",tre);
        return treRepository.save(tre);
    }

    /**
     * Return TRE object with all NdParent childes
     * @param id
     * @return
     */
    public TRE getTre(String id) throws ResourceNotFoundException {
        logger.called("getTre","id",id);
        TRE tre=treRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        List<NdParent> ndParents=ndParentRepository.findAll(Example.of(new NdParent(tre.ID)));
        ndParents.forEach(ndParent-> {
            try {
                tre.ndParents.add(getNdParent(ndParent.ID));
            } catch (ResourceNotFoundException e) {
                    logger.error("cannot find ndParentId "+ndParent.ID);
            }
        });
        return tre;
    }

    /**
     * Return NdParent object with all ND childes
     * @param id
     * @return
     */
    public NdParent getNdParent(String id) throws ResourceNotFoundException {
        logger.called("getNdParent","id",id);
        NdParent ndParent=ndParentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        List<ND> nds=ndRepository.findAll(Example.of(new ND(ndParent.ID)));
        nds.forEach(nd-> {
            try {
                ndParent.ND.add(getNd(nd.ID));
            } catch (ResourceNotFoundException e) {
                logger.error("cannot find ndId "+nd.ID);
            }
        });
        return ndParent;
    }

    /**
     * Return ND object with all FI childes
     * @param id
     * @return
     */
    public ND getNd(String id) throws ResourceNotFoundException {
        logger.called("getNd","id",id);
        ND nd=ndRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        List<FI> fis=fiRepository.findAll(Example.of(new FI(nd.ID)));
        fis.forEach(fi1->nd.FI.add(Helpers.initGson().fromJson(fi1.fiJson, FI.class)));
        return nd;
    }
    /**
     * Return FI object with all PD childes
     * @param id
     * @return
     */
    public FI getFi(String id) throws ResourceNotFoundException {
        logger.called("getFi","id",id);
        FI fi= fiRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return Helpers.initGson().fromJson(fi.fiJson, FI.class);
    }


    public FI findFI(String id) throws ResourceNotFoundException {
        logger.called("findFI","id",id);
        return fiRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public ND findNd(String id) throws ResourceNotFoundException {
        logger.called("findNd","id",id);
        return ndRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }
    public NdParent findNdParent(String id) throws ResourceNotFoundException {
        logger.called("findNdParent","id",id);
        return ndParentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public TRE findTre(String id) throws ResourceNotFoundException {
        logger.called("findTre","id",id);
        return treRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public List<TRE> getTres() throws ResourceNotFoundException {
        logger.called("getTres","","");
        List<TRE> tres=treRepository.findAll();
        for (TRE tre:tres){
            tre=getTre(tre.ID);
        }
        return tres;
    }


}



