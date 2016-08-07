package jesscampos.movielist;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jessica on 06/08/2016.
 */
public class Conexao {

    //armazenar os dados que o usuario solicitar
    public static String postDados(String urlUsuario, String parametrosUsuarios){
        URL url;
        HttpURLConnection connection = null;

        try{
            url = new URL(urlUsuario); //digitado pelo usuario como string
            connection = (HttpURLConnection)url.openConnection(); //abrir conexao
            connection.setRequestMethod("POST"); //qual metodo quero enviar a info: POST

            //content-type fala como quero que os dados sejam codificados: form
            //content-lenght fala quantos bytes quero passar
            //content-language fala a linguagem usada
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Lenght","" + Integer.toString(parametrosUsuarios.getBytes().length));
            connection.setRequestProperty("Content-Language","pt-BR");

            //desabilitar cache
            connection.setUseCaches(false);

            //habilitar entrada e saida de dados
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //armazenar dados de saida, escreve e finaliza
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(parametrosUsuarios);
            dataOutputStream.flush();
            dataOutputStream.close();

            //pegar dados de entrada
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")); //codificação de tratamento da escrita
            String linha;
            StringBuffer resposta = new StringBuffer();

            //pra montar o buffer, com todas as linhas necessarias
            while ((linha = bufferedReader.readLine()) != null){
                resposta.append(linha);
                resposta.append('\r');
            }
            bufferedReader.close();

            return resposta.toString();

        } catch (Exception erro){

            return null;
        } finally {
            //se conectado, desconecte
            if (connection != null){
                connection.disconnect();
            }
        }
    }
}
