package com.martinboy.demoexoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(new MainAdapter());
    }

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder>{

        @NonNull
        @Override
        public MainAdapter.MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list, parent, false);
            return new MainHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainHolder holder, int position) {

            Intent intent;

            switch (holder.getAdapterPosition()) {

                case 0:
                    holder.textView.setText("MP4 Steam Test");
                    intent = new Intent().setClass(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoType", 0);
                    break;
                case 1:
                    holder.textView.setText("Rtmp Steam Test");
                    intent = new Intent().setClass(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoType", 1);
                    break;
                case 2:
                    holder.textView.setText("HLS Steam Test");
                    intent = new Intent().setClass(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoType", 2);
                    break;
                case 3:
                    holder.textView.setText("Dash Steam Test");
                    intent = new Intent().setClass(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoType", 3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + holder.getAdapterPosition());
            }

            final Intent finalIntent = intent;
            holder.list_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(finalIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        class MainHolder extends RecyclerView.ViewHolder {

            LinearLayout list_bg;
            TextView textView;

            MainHolder(@NonNull View itemView) {
                super(itemView);
                list_bg = itemView.findViewById(R.id.list_bg);
                textView = itemView.findViewById(R.id.text_view);
            }
        }

    }

}
