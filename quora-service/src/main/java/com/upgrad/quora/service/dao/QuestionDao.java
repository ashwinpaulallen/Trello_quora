package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }


    public List<QuestionEntity> getAllQuestion() {
        try {
            return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<QuestionEntity> getAllQuestionsByUser(String userId){
        try {
            return entityManager.createNamedQuery("getAllQuestionsByUser", QuestionEntity.class).setParameter("uuid", userId).getResultList();
        } catch (NoResultException nre){
            return null;
        }
    }


    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionByUuid(String questionId){
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("questionId", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity editQuestion (QuestionEntity questionEntity){
        entityManager.merge(questionEntity);
        return questionEntity;
    }
}

