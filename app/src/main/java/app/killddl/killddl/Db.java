package app.killddl.killddl;

import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class Db {
    //save current user in DB class
    private FirebaseFirestore db;
    TextView errorMsg;
    private User user;
    private List<Tasks> taskList;
    private String uid;
    public Db() {
        db = FirebaseFirestore.getInstance();
    }
    public Db(String uid, List<Tasks> taskList){
        this.uid = uid;
        this.taskList = taskList;
    }
    public void setErrorMsgMain(TextView em) {
        this.errorMsg = em;
    }
    public void setID(String id){
        this.uid = id;
    }
    public void setUser(User u) {
        this.user = u;
    }
    public User getUser(){
        return this.user;
    }
    public List<Tasks> getTaskList(){
        return this.taskList;
    }
    public void setTaskList(List<Tasks> taskList){
        this.taskList = taskList;
    }

    public FirebaseFirestore getDB(){
        return this.db;
    }
    public boolean addUser(User user){
        this.db.collection("User").document(this.uid).set(user);
        return true;
    }
    public void addTask(Tasks task){
        this.taskList.add(task);
        db.collection("User").document(this.uid).collection("taskList").document(""+task.getId()).set(task);
    }

    public void removeTask(int taskId){
        taskList.get(taskId).isFinished = true;
        Tasks task = taskList.get(taskId);
        db.collection("User").document(this.uid).collection("taskList").document(""+task.getId()).set(task);
    }

    public void EditTask(int id,Tasks task){ //TODO need fixed
        for(Tasks t : taskList){
            if(t.getId() == id){
                taskList.remove(t);
                taskList.add(task);
            }
        }
        db.collection("User").document(this.uid).collection("taskList").document(""+task.getId()).set(task);

    }

    public List<Tasks> getTaskListByTime(Timestamp tsp){
        List<Tasks> selected = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(tsp.toDate());
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DATE);
        for(Object t : this.taskList){
            Timestamp curr = ((Tasks) t).getDeadline();
            Calendar mCal = Calendar.getInstance();
            mCal.setTime(curr.toDate());
            int currYear = mCal.get(Calendar.YEAR);
            int currMonth = mCal.get(Calendar.MONTH);
            int currDay = mCal.get(Calendar.DATE);
            if(mYear == currYear && mMonth == currMonth && mDay == currDay){
                selected.add((Tasks) t);
            }
        }
        return selected;
    }

    public int getUnfinieshedTask(){
        int i = 0;
        for (Tasks t: taskList
             ) {
            if(!t.isFinished) i++;
        }
        return i;
    }


}
