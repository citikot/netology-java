package task2;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class NasaApiTester {
    public static final String URI =
            "https://api.nasa.gov/planetary/apod?api_key=ptF7bi9zANvEfl2SX9qbet1Iiv20uytKWKhvkplH";

    //Сущность, которая будет преобразовывать ответ в наш объект NASA
    public static final ObjectMapper mapper = new ObjectMapper();

        public static void main(String[] args) throws IOException {

            //Настраиваем наш HTTP клиент, который будет отправлять запросы
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            //Отправляем запрос и получаем ответ
            CloseableHttpResponse response = httpClient.execute(new HttpGet(URI));

            //Преобразуем ответ в Java-объект NasaObject
            NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
            System.out.println(nasaObject);

            // Отправляем запрос и получаем ответ с нашей картинкой
            CloseableHttpResponse pictureResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));

            //Формируем автоматически название для файла
            String[] arr = nasaObject.getUrl().split("/");
            String file = arr[6];

            //Проверяем что наш ответ не null
            HttpEntity entity = pictureResponse.getEntity();
            try (FileOutputStream fos = new FileOutputStream(file)){
                entity.writeTo(fos);
            }

    }
}
