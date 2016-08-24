package com.pineone.icbms.so.contextmodel.logic;

import com.pineone.icbms.so.contextmodel.entity.ContextModel;
import com.pineone.icbms.so.contextmodel.proxy.ContextModelExProxy;
import com.pineone.icbms.so.contextmodel.proxy.ContextModelInProxy;
import com.pineone.icbms.so.contextmodel.ref.ContextType;
import com.pineone.icbms.so.contextmodel.ref.ResponseMessage;
import com.pineone.icbms.so.contextmodel.store.ContextModelStore;
import com.pineone.icbms.so.domain.entity.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by melvin on 2016. 8. 1..
 */

@Service
public class ContextModelLogicImpl implements ContextModelLogic{

    @Autowired
    ContextModelExProxy contextModelExProxy;

    @Autowired
    ContextModelStore contextModelStore;

    @Autowired
    ContextModelInProxy contextModelInProxy;


    public static final Queue CONTEXT_MODEL_QUEUE = new LinkedList<>();

    public static ContextModelLogicImpl newContextModelLogic(){
        return new ContextModelLogicImpl();
    }

    //NOTE: ContextInformationLogic 컴포넌트에서 퍼블리싱한 정보를 이용해서 ContextInformationLogic 리스트 조회
    public List<String> retrieveContextInformationNameList(){
        //
//        ContextModelExProxy contextModelExProxy = new ContextModelInternalProxy().newContextModelInternalProxy();
        List<String> contextInformationList = contextModelInProxy.retrieveContextInformationNameList();
        return contextInformationList;
    }

    //NOTE: ContextModel 의 ContextType 을 결정하기 위해 ContextTypeList 제공
    public List<ContextType> retrieveContextTypeList(){
        //
        ContextTypeLogic contextTypeLogic = ContextTypeLogicImpl.newContextTypeLogic();
        List<ContextType> contextTypeList = contextTypeLogic.retrieveContextTypeList();
        return contextTypeList;
    }

    //NOTE: ContextModel 등록정보를 수신받고 TODO : SO DB에 저장하고 SDA 에 등록하며 보여줘야할 내용(결정 필요)을 리턴
    public String registerContextModel(ContextModel contextModel){
        //
        ResponseMessage responseMessage = ResponseMessage.newResponseMessage();
//        ContextModelStore contextModelStore = ContextModelMapStore.getInstance();
        //ContextModelExProxy contextModelExProxy = ContextModelSDAProxy.newContextModelProxy();

        contextModelStore.createContextModel(contextModel);
        //contextModelExProxy.registerContextModel(contextModel);
        String contextModelResultMessage = responseMessage.contextModelResultMessage(contextModel);
        return contextModelResultMessage;
    }

    // NOTE: DB 에서 ContextModel을 조회해서 Name List 로 변환
    public List<String> retrieveContextModelNameList() {
        //
//        ContextModelStore contextModelStore = ContextModelMapStore.getInstance();
        List<ContextModel> contextModelList = contextModelStore.retrieveContextModelList();

        //SDA 에서 조회할 경우
        //ContextModelExProxy contextModelExProxy = ContextModelSDAProxy.newContextModelProxy();
        //List<ContextModel> contextModelList = contextModelExProxy.retrieveContextModelListFromSDA();
        List<String> contextModelNameList = new ArrayList<>();
        for(ContextModel contextModel : contextModelList){
            contextModelNameList.add(contextModel.getName());
        }
        return contextModelNameList;
    }

    // NOTE : DB 에서 ContextModel 상세 조회
    public ContextModel retrieveContextModelDetail(String contextModelName) {
        //
//        ContextModelStore contextModelStore = ContextModelMapStore.getInstance();
        ContextModel contextModel = contextModelStore.retrieveContextModelDetail(contextModelName);
        //SDA 에서 조회할 경우
        //ContextModelExProxy contextModelExProxy = ContextModelSDAProxy.newContextModelProxy();
        //ContextModel contextModel = contextModelExProxy.retrieveContextModelDetail(contextModelName);
        return contextModel;
    }

    //NOTE: ContextModel 상황 발생 여부 질의
    public List<String> isHappenContextModel(String contextModelId){
        //
//        ContextModelExProxy contextModelExProxy = ContextModelSDAProxy.newContextModelProxy();
        List<String> domainIdList = contextModelExProxy.retrieveContextModelEvent(contextModelId);
        return domainIdList;
    }

    //NOTE : ContextModel 이름으로 ContextModelType 조회
    @Override
    public String retrieveContextModelType(String contextModelName) {
        //
//        ContextModelStore contextModelStore = ContextModelMapStore.getInstance();
        ContextModel contextModel = contextModelStore.retrieveContextModelDetail(contextModelName);
        return contextModel.getContextType();
    }

    //NOTE: EmergencyContextmodel 을 일시적으로 Queue 에 저장. Profile Component 에서 사용 할 수 QUEUE 에 삽입
    @Override
    public String useQueueSaveContextModel(ContextModel contextModel) {
        //
        ResponseMessage responseMessage = ResponseMessage.newResponseMessage();

        CONTEXT_MODEL_QUEUE.offer(contextModel);

        String contextModelResultMessage = responseMessage.contextModelResultMessage(contextModel);
        return contextModelResultMessage;
    }

    @Override
    public ContextModel retrieveQueueData() {
        ContextModel contextModel = (ContextModel) ContextModelLogicImpl.CONTEXT_MODEL_QUEUE.poll();
        return contextModel;
    }

    @Override
    public List<Domain> retrieveDomainList() {
//        ContextModelExProxy contextModelExProxy = ContextModelInternalProxy.newContextModelInternalProxy();
        List<Domain> domainList = contextModelInProxy.retrieveDomainList();
        return domainList;
    }

    @Override
    public List<String> retrieveContextModelIdList() {
        //
        List<ContextModel> contextModelList = contextModelStore.retrieveContextModelList();

        //SDA 에서 조회할 경우
        //ContextModelExProxy contextModelExProxy = ContextModelSDAProxy.newContextModelProxy();
        //List<ContextModel> contextModelList = contextModelExProxy.retrieveContextModelListFromSDA();
        List<String> contextModelIdList = new ArrayList<>();
        for(ContextModel contextModel : contextModelList){
            contextModelIdList.add(contextModel.getId());
        }
        return contextModelIdList;
    }
}
