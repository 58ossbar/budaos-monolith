package com.budaos.common.validator.group;

import javax.validation.GroupSequence;

/**
 * Title:定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验 Group
 * Copyright: Copyright (c) 2017
 * Company:budaos.co.,ltd
 * 
 * @author budaos.co.,ltd
 * @version 1.0
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
