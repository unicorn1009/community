package com.cheng.community.service;

import com.cheng.community.dto.PaginationDTO;
import com.cheng.community.dto.QuestionDTO;
import com.cheng.community.mapper.QuestionMapper;
import com.cheng.community.mapper.UserMapper;
import com.cheng.community.model.Question;
import com.cheng.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        // 查数据库获取总问题数
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);
        if (page < 1) page = 1;
        if (page > paginationDTO.getTotalPage()) page = paginationDTO.getTotalPage();

        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        // 将问题列表装入paginationDTO
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
