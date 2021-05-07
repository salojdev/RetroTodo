package com.saloj.prosaloj.todo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saloj.prosaloj.todo.API.APIRequestData;
import com.saloj.prosaloj.todo.API.RetroServer;
import com.saloj.prosaloj.todo.Model.DataModel;
import com.saloj.prosaloj.todo.Model.ResponseModel;
import com.saloj.prosaloj.todo.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.HolderData>{

    private Context ctx;
    private List<DataModel> listModel;
    private int taskid;
    String strTask;

private MyListener ml;

    public TaskAdapter(Context ctx, List<DataModel> listModel, MyListener ml) {
        this.ctx = ctx;
        this.listModel = listModel;
        this.ml = ml;
    }


    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);

        HolderData holderData = new HolderData(layout);
        return holderData;

    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listModel.get(position);

       holder.tvId.setText(String.valueOf(dm.getTaskid()));
        holder.tvTask.setText(dm.getTask());
        holder.tvDate.setText(dm.getCreated_at());

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ml.callback(dm.getTaskid(),dm.getTask());
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listModel.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvTask,tvDate;
        ImageView imgDelete,imgUpdate;
        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvTask = itemView.findViewById(R.id.tv_task);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgUpdate = itemView.findViewById(R.id.img_edit);


            imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(
                            ctx);
                    // get item_add_prompt.xml view

                    LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View promptsView = vi.inflate(R.layout.task_edit_prompt, null);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.edt_task_edit);
                    Button okBtn = (Button) promptsView.findViewById(R.id.btnSaveEdit);
                    ImageView cancelBtn = (ImageView) promptsView.findViewById(R.id.btnCancel);

                    // set item_add_prompt.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    // create alert dialog
                    final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);

                    // set dialog message
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get user input and set it to result
                            taskid = Integer.parseInt(tvId.getText().toString());

                            strTask = userInput.getText().toString();

                            Toast.makeText(ctx, "position"+taskid, Toast.LENGTH_SHORT).show();
                         //   MyLogicToIntimateOthers();
                            editTask();
                       /* list.add(0,name);
                        //Collections.reverse(list);
                        // Collections.sort(list,Collections.reverseOrder());
                        //  Collections.sort(list);
                        listItem.setAdapter(adapter);
                        adapter.notifyDataSetChanged();*/
                            alertDialog.dismiss();

                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    // set dialog in bottom
                    Window dialogWindow = alertDialog.getWindow();
                    dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                    // show it
                    alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();
                }

            });


            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(ctx);

                    builder.setMessage("Are you sure delete user.");
                    builder.setTitle("Alert!");
                     builder.setIcon(R.drawable.person);
                    builder.setCancelable(true);
                    taskid = Integer.parseInt(tvId.getText().toString());

                    builder
                            .setPositiveButton(
                                    "Ok",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                              Toast.makeText(ctx, "position"+taskid, Toast.LENGTH_SHORT).show();
                                              deleteData();
                                            dialog.dismiss();
                                            //     ((MainActivity)ctx).retrieveData();

                                        }
                                    });

                    builder
                            .setNegativeButton(
                                    "Cancel",
                                    new DialogInterface
                                            .OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.cancel();

                                        }
                                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

    }

    private void editTask() {

        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> delData = ardData.ardUpdateData(taskid,strTask);

        delData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();

                if (response.isSuccessful() && success == 1) {

                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                }
                else if (response.isSuccessful() &&  success == 0){
                    Log.e("response", "updatefailed :" +message);
                    Toast.makeText(ctx, "UpdateTask :"+ message, Toast.LENGTH_SHORT).show();

                }
                //  Toast.makeText(ctx, "DeleteTask :"+message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Retro Not Connected."+t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void deleteData(){
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> delData = ardData.ardDeleteData(taskid);

        delData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                int success = response.body().getSuccess();
                String message = response.body().getMessage();

                if (response.isSuccessful() && success == 1) {
                    notifyDataSetChanged();
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();

                }
                else if (response.isSuccessful() &&  success == 0){
                    Log.e("response", "detletfailed :" +message);
                    Toast.makeText(ctx, "DeleteTask :"+ message, Toast.LENGTH_SHORT).show();

                }
              //  Toast.makeText(ctx, "DeleteTask :"+message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(ctx, "Retro Not Connected."+t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

   /* public static interface MyListener {
        void callback(int taskid, String task);

        //Invoke the interface
     //   ml.callback(taskid,strTask);

    }*/
}
