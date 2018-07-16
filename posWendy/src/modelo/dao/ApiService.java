package modelo.dao;

import io.reactivex.Observable;
import modelo.Seccion;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
	@GET("secciones/{id}")
    Observable<Seccion> getSeccion(@Path("id") Integer id);
}
