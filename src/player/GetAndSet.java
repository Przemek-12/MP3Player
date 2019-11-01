package player;

public class GetAndSet {

    private String title;
    private String path2;

    public GetAndSet(String title){
        this.title=title;
    }

    public GetAndSet(String title, String path2){

        this.path2=path2;
        this.title=title;
    }

    public void setPath(String path){
        this.path2=path;
    }

    public String getPath(){
        return path2;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getTitle(){
        return title;
    }
}
