package com.yxinmiracle.graph.utils;


import com.yxinmiracle.model.graph.ConnectNode;
import com.yxinmiracle.model.graph.NodeAndConnectNode;
import com.yxinmiracle.model.serives.dtos.CourseTagAndTagPropertyDto;
import com.yxinmiracle.model.serives.pojos.CourseTag;
import com.yxinmiracle.model.serives.pojos.Relation;
import org.neo4j.driver.*;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.Values.parameters;


/**
 * @version 1.0
 * @author： YxinMiracle
 * @date： 2021-09-21 17:48
 */
public class GraphUtils {

    private static final String uri = "bolt://175.178.60.127:7687";
    private static final String username = "neo4j";
    private static final String password = "password";

    private static Driver driver;
    private static Session session;

    static {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        session = driver.session();
    }


    public static void createNode(com.yxinmiracle.model.graph.Node node) {
        try (Transaction tx = session.beginTransaction()) {
            String label = node.getLabel();
            String sql = "CREATE (n:`" + label + "` {nodeName: $nodeName,courseId: $courseId,desc: $desc,courseName: $courseName,tagId:$tagId,isDelete:0})";
            System.out.println(sql);
            tx.run(sql, parameters("nodeName", node.getNodeName(),
                    "courseId", node.getCourseId(),
                    "desc", node.getDesc(), "courseName", node.getCourseName(), "tagId", node.getCourseTagId()));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void getNodeByLabelAndNodeName(String nodeName, String label) {
//        try (Transaction tx = session.beginTransaction()) {
//            String sql = "MATCH (n:`" + label + "`) WHERE n.nodeName={nodeName} return n";
//            StatementResult result = tx.run(sql, parameters("nodeName", nodeName));
//            while (result.hasNext()) {
//                Record record = result.next();
//                Map<String, Object> stringObjectMap = record.asMap();
//                for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
//                    Node node = (Node) stringObjectEntry.getValue();
//                    System.out.println(node.asMap());
//                }
//            }
//            tx.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static List<Map> getNodesByLabelName(String labelName) {
//        List<Map> nodeList = new ArrayList<>();
//        try (Transaction tx = session.beginTransaction()) {
//            String sql = "MATCH (n:`" + labelName + "`) return n";
//            StatementResult result = tx.run(sql);
//            while (result.hasNext()) {
//                Record record = result.next();
//                Map<String, Object> stringObjectMap = record.asMap();
//                for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
//                    Node node = (Node) stringObjectEntry.getValue();
//                    nodeList.add(node.asMap());
//                }
//            }
//            tx.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return nodeList;
//    }

    public static void createRelationNode(ConnectNode connectNode) {
        try (Transaction tx = session.beginTransaction()) {
            String label = connectNode.getLabel();
            System.out.println("添加关系啦" + label);
            String sql = "CREATE (n:`" + label + "` {knowledgePointStartName:$knowledgePointStartName,knowledgePointEndName:$knowledgePointEndName,relationName:$relationName,courseName:$courseName})";
            tx.run(sql, parameters("knowledgePointStartName", connectNode.getKnowledgePointStartName(), "knowledgePointEndName", connectNode.getKnowledgePointEndName(), "relationName", connectNode.getRelationName(), "courseName", connectNode.getCourseName()));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * exp:
     * match (n:Nginx_relation),(m:Nginx),(p:Nginx) where n.parentNodeName=m.name and p.name=n.childNodeName
     * create (m)-[:Nginx_relation {relationValue:n.relationValue}]->(p)
     * return m,n,p
     * 创建真正的关系边
     *
     * @param nodeAndConnectNode nodeAndConnectNode
     */
    public static void addRealRelation(NodeAndConnectNode nodeAndConnectNode) {
        try (Transaction tx = session.beginTransaction()) {
            String label = nodeAndConnectNode.getNodeLabelName();
            String relationShipName = nodeAndConnectNode.getConnectNodeLabelName(); // relation
            String sql = "MATCH (n:`" + relationShipName + "`),(m:`" + label + "`),(p:`" + label + "`) where n.knowledgePointStartName=m.nodeName and p.nodeName=n.knowledgePointEndName MERGE  (m)-[:`" + relationShipName + "` {relationName:n.relationName}]->(p)";
            tx.run(sql);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteNode(Integer tagId){
        try (Transaction tx = session.beginTransaction()) {
            String sql = "MATCH (n {tagId:$tagId}) set n.isDelete=1";
            tx.run(sql,parameters("tagId",tagId));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resumeNode(Integer tagId){
        try (Transaction tx = session.beginTransaction()) {
            String sql = "MATCH (n {tagId:$tagId}) set n.isDelete=0";
            tx.run(sql,parameters("tagId",tagId));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCourseAllGraphCypher(String courseName) {
        String cypher = "MATCH (n:`" + courseName + "` {isDelete:0})-[r]->(p {isDelete:0}) return *";
        return cypher;
    }

    public static String getCourseTagCypherByCourseNameAndTagName(String courseName, String TagName) {
        String cypher = "MATCH (n:`" + courseName + "` {name:'" + TagName + "'})-[r]-(p) return *";
        return cypher;
    }

    public static void updateRelationName(Relation relation,String courseName) {
        try (Transaction tx = session.beginTransaction()) {
            String knowledgePointStartName = relation.getKnowledgePointStartName();
            String knowledgePointEndName = relation.getKnowledgePointEndName();
            String relationName = relation.getRelationName();
            System.out.println(knowledgePointStartName+":"+knowledgePointEndName+":"+relationName+":"+courseName);
            String cypher = "match (a:`" + courseName + "` {nodeName:$knowledgePointStartName})-[r]->(p {nodeName:$knowledgePointEndName}) set r.relationName=$relationName";
            tx.run(cypher,parameters("knowledgePointStartName",knowledgePointStartName,"knowledgePointEndName",knowledgePointEndName,"relationName",relationName));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateNodeNameAndAddProperty(CourseTagAndTagPropertyDto dto) {
        CourseTag courseTag = dto.getNewTagData();
        Integer courseTagId = courseTag.getCourseTagId();
        String courseName = courseTag.getCourseName();
        String tagName = courseTag.getTagName();
        String tagDesc = courseTag.getTagDesc();
        Map<String, String> retPropertyMap = dto.getRetPropertyMap();
        // 更新知识点的名称和描述
        try (Transaction tx = session.beginTransaction()) {
            String updateTagNameCypher = "MATCH (n:`" + courseName + "` {tagId:$courseTagId}) set n.nodeName=$tagName, n.desc=$tagDesc";
            tx.run(updateTagNameCypher,parameters("courseTagId",courseTagId,"tagName",tagName,"tagDesc",tagDesc));
            for (Map.Entry<String, String> stringStringEntry : retPropertyMap.entrySet()) {
                String propertyKey = stringStringEntry.getKey();
                String propertyValue = stringStringEntry.getValue();
                String updatePropertyCypher = "MATCH (n:`" + courseName + "` {tagId:$courseTagId}) set n."+propertyKey+"=$propertyValue";
                tx.run(updatePropertyCypher,parameters("courseTagId",courseTagId,"propertyValue",propertyValue));
            }
            tx.commit();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Integer nodeTagId, String newTagName, String courseName, Map<String, String> propertyMap
//    public static void updateNodeNameAndAddProperty(UpdateNodeNameAndPropertyDto updateNodeNameAndPropertyDto) {
//        Integer nodeTagId = updateNodeNameAndPropertyDto.getNodeTagId();
//        String newTagName = updateNodeNameAndPropertyDto.getNewTagName();
//        String courseName = updateNodeNameAndPropertyDto.getCourseName();
//        Map<String, String> propertyMap = updateNodeNameAndPropertyDto.getPropertyMap();
//        String desc = updateNodeNameAndPropertyDto.getDesc();
//        if (StringUtils.isEmpty(desc)){
//            desc = " ";
//        }
//        try (Transaction tx = session.beginTransaction()) {
//            String updateNameCypher = "MATCH (n:`" + courseName + "` {tid:{nodeTagId}}) set n.name={newTagName}";
//            tx.run(updateNameCypher, parameters("nodeTagId", nodeTagId, "newTagName", newTagName));
//            String updateDescCypher = "MATCH (n:`" + courseName + "` {tid:{nodeTagId}}) set n.desc={desc}";
//            tx.run(updateDescCypher, parameters("nodeTagId", nodeTagId, "desc", desc));
//            for (Map.Entry<String, String> stringStringEntry : propertyMap.entrySet()) {
//                String propertyKey = stringStringEntry.getKey();
//                String propertyValue = stringStringEntry.getValue();
//                String updatePropertyCypher = "MATCH (n:`" + courseName + "` {tid:{nodeTagId}}) set n."+propertyKey+"={propertyValue}";
//                System.out.println(updatePropertyCypher);
//                tx.run(updatePropertyCypher,parameters("nodeTagId",nodeTagId,"propertyValue",propertyValue));
//            }
//            tx.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public static void main(String[] args) {
//        ClassEmpty classEmpty = new ClassEmpty();
//        classEmpty.setTid(100);
//        classEmpty.setCourseName("testclass");
//        classEmpty.setName("多线程");
//        classEmpty.setDesc("什么东西没有");
//        classEmpty.setLabel("testclass");
//        createNode(classEmpty);
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        session = driver.session();
        String label = "hah";
        try (Transaction tx = session.beginTransaction()) {
            String sql = "CREATE (n:`" + label + "` {knowledgePointStartName:$knowledgePointStartName,knowledgePointEndName:$knowledgePointEndName,relationName:$relationName,courseName:$courseName})";
            tx.run(sql, parameters("knowledgePointStartName", "shsad", "knowledgePointEndName", "saasd", "relationName", "sadasd", "courseName", "python"));
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
