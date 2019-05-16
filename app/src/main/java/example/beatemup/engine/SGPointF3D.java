package example.beatemup.engine;

public class SGPointF3D {

	public float x;
	public float y;
	public float z;
	
	public SGPointF3D(){}
	
	public SGPointF3D(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final void set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final void set(SGPointF3D p) { 
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }
}
