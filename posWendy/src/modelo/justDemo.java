package modelo;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class justDemo {
	
	private Subscription subscription;
	
	public static void main(String []args){
		Observable.just("Hello","goodBye").subscribe(new Consumer<String>(){

			@Override
			public void accept(String arg0) throws Exception {
				// TODO Auto-generated method stub
				System.out.println(arg0);
				
				
				
			}
			
		});
		
		
	}

}
