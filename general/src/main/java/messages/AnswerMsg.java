package messages;

import java.io.Serializable;

public class AnswerMsg implements Serializable {
    private String Msg;
    private Status status;
    public AnswerMsg(){
        clearMessage();
    }

    public void clearMessage(){
        Msg = "";
    }

    public void AddAnswer(String str){
        Msg += str + "\n";
    }

    public void AddErrorMsg(String str){
        Msg += "error: " + str;
    }

    public void AddStatus(Status st){
        status = st;
    }

    public String getMessage(){
        return Msg;
    }

    public Status getStatus(){
        return status;
    }



}
