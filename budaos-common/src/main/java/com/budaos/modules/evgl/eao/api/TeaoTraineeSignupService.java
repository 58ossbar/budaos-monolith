package com.budaos.modules.evgl.eao.api;

import com.budaos.common.exception.BudaosException;
import com.budaos.modules.evgl.eao.domain.TeaoTraineeSignup;

public interface TeaoTraineeSignupService {

    void save(TeaoTraineeSignup teaoTraineeSignup) throws BudaosException;

}
