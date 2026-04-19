package com.budaos.modules.evgl.eao.service;

import com.budaos.common.exception.BudaosException;
import com.budaos.modules.evgl.eao.api.TeaoTraineeSignupService;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeSignup;
import com.budaos.modules.evgl.eao.persistence.TeaoTraineeSignupMapper;
import com.budaos.utils.tool.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeaoTraineeSignupServiceImpl implements TeaoTraineeSignupService {

    @Autowired
    private TeaoTraineeSignupMapper teaoTraineeSignupMapper;

    @Override
    public void save(TeaoTraineeSignup teaoTraineeSignup) throws BudaosException {
        teaoTraineeSignup.setSignupId(Identities.uuid());
        teaoTraineeSignupMapper.insert(teaoTraineeSignup);
    }
}
