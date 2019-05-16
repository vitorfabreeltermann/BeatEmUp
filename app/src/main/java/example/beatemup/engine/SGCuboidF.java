package example.beatemup.engine;

public class SGCuboidF {
	
	public float left;
    public float top;
    public float right;
    public float bottom;
    public float front;
    public float back;
    
    public SGCuboidF(){}
    
    public SGCuboidF(float left, float top, float front, float right, float bottom, float back) {
        this.left 	= left;
        this.top 	= top;
        this.right 	= right;
        this.bottom = bottom;
        this.front 	= front;
        this.back 	= back;
    }
    
    public void set(float left, float top, float front, float right, float bottom, float back) {
        this.left 	= left;
        this.top 	= top;
        this.right 	= right;
        this.bottom = bottom;
        this.front 	= front;
        this.back 	= back;
    }
    
    public void set(SGCuboidF par) {
        this.left   = par.left;
        this.top    = par.top;
        this.right  = par.right;
        this.bottom = par.bottom;
        this.front 	= par.front;
        this.back 	= par.back;
    }
}
