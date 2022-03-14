package com.yxinmiracle.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxinmiracle.model.serives.dtos.QuestionDto;
import com.yxinmiracle.model.serives.pojos.Question;
import com.yxinmiracle.model.serives.vos.ChoiceQuestionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    public List<ChoiceQuestionVo> getChoiceList(QuestionDto dto);

    public Integer getChoiceListCount(QuestionDto dto);


}
