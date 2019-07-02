/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickquiz;

/**
 *
 * @author Bailey S-P
 */
public class Words {
    
    String QNum;
    String Topic;
    String Ans;
    String CorrectAns;

    public Words() {
        QNum = "First";
        Topic = "Second";
        Ans = "";
        CorrectAns = "";
    }
    
    public Words(String QNum, String Topic, String Ans, String CorrectAns) {
        this.QNum = QNum;
        this.Topic = Topic;
        this.Ans = Ans;
        this.CorrectAns = CorrectAns;
    }

    Words(String QNum, String Topic, String CorrectAns) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getQNum() {
        return QNum;
    }

    public void setQNum(String QNum) {
        this.QNum = QNum;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String Topic) {
        this.Topic = Topic;
    }

    public String getAns() {
        return Ans;
    }

    public String getCorrectAns() {
        return CorrectAns;
    }

    public void setCorrectAns(String CorrectAns) {
        this.CorrectAns = CorrectAns;
    }
    
}
