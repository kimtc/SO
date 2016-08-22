package com.pineone.icbms.so.profile.logic;

import com.pineone.icbms.so.bizcontext.pr.BizContextPresentation;
import com.pineone.icbms.so.contextmodel.entity.ContextModel;
import com.pineone.icbms.so.contextmodel.logic.ContextModelLogicImpl;
import com.pineone.icbms.so.contextmodel.pr.ContextModelPresentation;
import com.pineone.icbms.so.contextmodel.ref.ContextType;
import com.pineone.icbms.so.profile.entity.Profile;
import com.pineone.icbms.so.profile.proxy.ProfileInternalProxy;
import com.pineone.icbms.so.profile.proxy.ProfileProxy;
import com.pineone.icbms.so.profile.ref.ResponseMessage;
import com.pineone.icbms.so.profile.store.ProfileMapStore;
import com.pineone.icbms.so.profile.store.ProfileStore;
import com.pineone.icbms.so.servicemodel.pr.ServiceModelPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melvin on 2016. 8. 11..
 * NOTE: Profile 생성 및 실행 로직 포함
 */
@Service
public class ProfileLogicImpl implements ProfileLogic, Runnable{
    //
    @Autowired
            ContextModelPresentation contextModelPresentation;

    @Autowired
    ProfileProxy profileProxy;

    @Autowired
    ProfileStore profileStore;

//    ContextModelPresentation contextModelPresentation = new ContextModelPresentation();

    public static ProfileLogicImpl newProfileLogic() {
        return new ProfileLogicImpl();
    }

    //NOTE : ContextModel Component 의 프레젠테이션을 이용하여 SO에 등록된 ContextModel 들의 이름들을 받아옴
    @Override
    public List<String> retrieveContextModelNameList() {
        //
//        ProfileProxy profileProxy = ProfileInternalProxy.newProfileInternalProxy();
        List<String> contextModelNameList = profileProxy.retrieveContextModelNameList();
        return contextModelNameList;
    }

    //NOTE : ServiceModel Component 의 프레젠테이션을 이용하여 SO에 등록된 ServiceModel 들의 이름들을 받아옴
    @Override
    public List<String> retrieveServiceModelNameList() {
        //
//        ProfileProxy profileProxy = ProfileInternalProxy.newProfileInternalProxy();
        List<String> serviceModelNameList = profileProxy.retrieveServiceModelNameList();
        return serviceModelNameList;
    }

    //NOTE : BizComponent 의 프레젠테이션을 이용하여 SO에 등록된 BizContext 의 이름들을 받아옴
    @Override
    public List<String> retrieveBizContextNameList() {
        //
//        ProfileProxy profileProxy = ProfileInternalProxy.newProfileInternalProxy();
        List<String> bizContextTypeList = profileProxy.retrieveBizContextList();
        return bizContextTypeList;
    }

    //NOTE : Profile 등록 정보를 수신받고 SO DB에 저장하고 보여줘야할 내용(ResponseMessage)을 리턴
    // TODO : 컨텍스트모델에서 Type 구분후 등록받으면 변경
    @Override
    public String registerProfile(Profile profile) {
        //
        ResponseMessage responseMessage = ResponseMessage.newResponseMessage();
//        ProfileStore profileStore = ProfileMapStore.getInstance();

        //NOTE: ScheduleType 인데 스케쥴이 설정 안되있는 경우
        String contextModelType = contextModelPresentation.retrieveContextModelType(profile.getContextModelName());
        if(contextModelType.equals(ContextType.ScheduleType.toString())){
            if(profile.getPeriod() == 0){
                String profileResultMessage = "Input ScheduleTime";
                return profileResultMessage;
            }
            else{
                // TODO : 스케쥴러에 등록 - (Profile 이름, 스케쥴)
            }
        }
        profileStore.createProfile(profile);
        String profileResultMessage = responseMessage.profileResultMessage(profile);
        return profileResultMessage;
    }

    //NOTE: 저장된 Profile List 조회
    @Override
    public List<String> retrieveProfileNameList() {
        //
//        ProfileStore profileStore = ProfileMapStore.getInstance();
        List<String> profileNameList = new ArrayList<>();
        List<Profile> profileList = profileStore.retrieveProfileList();

        for(Profile profile : profileList){
            profileNameList.add(profile.getName());
        }
        return profileNameList;
    }

    //NOTE: DB에 저장된 Profile 상세 내역 조회
    @Override
    public Profile retrieveProfileDetail(String profileName) {
        //
//        ProfileStore profileStore = ProfileMapStore.getInstance();
        Profile profile = profileStore.retrieveProfileDetail(profileName);
        return profile;
    }

    //NOTE : ContextModel QUEUE 에 ContextModel 이 있다면 로직 실행
    @Override
    public void run() {
        //
//        ProfileProxy profileProxy = ProfileInternalProxy.newProfileInternalProxy();
        while(true){
            try{
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!(profileProxy.checkContextModelQueue())){
                ContextModel contextModel = profileProxy.retrieveContextModelQueueData();
                System.out.println(contextModel.getName());
                //TODO : 디비 연결후 contextModel 이름으로 Profile 조회 기능 구현 및 연결
                //TODO : Profile 로 ServiceModel 찾아서 ServiceModel 에 전송
            }
        }
    }
}