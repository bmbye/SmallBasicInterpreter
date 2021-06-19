public class GraphicsWindow extends Token{
    protected String title,FontName;
    protected int  height, width, CanResize, FontBold;  

    public GraphicsWindow(){
        super("Object", "GraphicsWindow");
    }

    public void setTitle(String t){
        this.title = t;
    }

    public void setHeight(int t){
        this.height = t;
    }

    public void setWidth(int t){
        this.width = t;
    }

    public void setResizable(int t){
        this.CanResize = t;
    }

    public void setFontName(String t){
        this.FontName = t;
    }

    public void setFontBold(int t){
        this.FontBold = t;
    }
}
