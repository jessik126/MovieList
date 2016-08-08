package jesscampos.movielist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "jesscampos.movielist.MESSAGE";

    EditText edMovie;
    Button btnProcurar;
    String url = "";
    String parametros = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables just here
        edMovie = (EditText)findViewById(R.id.edMovie);
        btnProcurar = (Button) findViewById(R.id.btnProcurar);


        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pra ver o estado da rede -- executa em segundo plano
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data

                    String title = edMovie.getText().toString();

                    //verifica se os campos nao estao vazios e realiza o login
                    if(title.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
                    } else {
                        url = "http://192.168.1.104/movie/imdb.php";
                        parametros = "title="+ title;
                        new SolicitaDados().execute(url);

                    }

                } else {
                    // display error
                    Toast.makeText(getApplicationContext(),"Nenhuma conexão detectada",Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    //atividade assincrona de assegurar a conexao
    private class SolicitaDados extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Conexao.postDados(urls[0],parametros);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override

        protected void onPostExecute(String result) {
           if(result.contains("Title")){
               Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_LONG).show();

                //abrir tela Main
               Intent intent = new Intent(MainActivity.this,DisplayMovie.class);
               String message = result.toString();
               intent.putExtra(EXTRA_MESSAGE, message);
               startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
