package com.example.apphx;

import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.repository.ILocalUsersRepo;
import com.example.apphx.model.repository.IRemoteUserRepo;

/**
 * 使用外观模式在构建远程仓库的一个初始化工作
 * Created by Administrator on 2016/11/8 0008.
 */

public class HxModuleInitializer {

    private static HxModuleInitializer sInstace;

    /**
     * 返回模块的初始化工作
     *
     * @return
     */
    public static HxModuleInitializer getsInstace() {
        if (sInstace == null) {
            sInstace = new HxModuleInitializer();
        }
        return sInstace;
    }


    /**
     * 分别对远程仓库和本地仓库进行一个初始化
     *
     * @param remoteUserRepo
     * @param localUsersRepo
     */
    public void init(IRemoteUserRepo remoteUserRepo, ILocalUsersRepo localUsersRepo) {
        HxContactManager.getInstance()
                .initRemoteUserRepo(remoteUserRepo)
                .initLocalUserRepo(localUsersRepo);
    }
}
