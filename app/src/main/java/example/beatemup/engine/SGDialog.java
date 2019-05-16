package example.beatemup.engine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class SGDialog {
	private AlertDialog mDialog;
	
	public SGDialog(Context context, int messageId, int okId, int cancelId){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setMessage(messageId)
			.setPositiveButton(okId, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					onOk();
				}
			})
			.setNegativeButton(cancelId, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int id) {
					onCancel();
				}
			});
		mDialog = builder.create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	public void onOk(){}
	
	public void onCancel(){}
	
	public void show(){
		mDialog.show();
	}
}
