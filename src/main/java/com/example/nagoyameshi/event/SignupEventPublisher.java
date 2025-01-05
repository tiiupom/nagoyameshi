package com.example.nagoyameshi.event;
 
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;

/* Publisherクラス　イベントを発生させたい処理の中で呼び出して使う
 * ＠ComponentでDIコンテナに登録し、呼び出すクラスに対しDIを行えるようにする
 * PublishEvent() ApplicationEventPublisherインターフェースが提供する機能
 * 		引数に発行したいEventクラスのインスタンスを渡し、イベントを発生させたいタイミングでメソッドを呼び出す
 * 		「publishSignupEvent」はAuthControllerクラスのsignup()内（POST)で呼び出し、
 * 		ユーザーの会員登録が完了したタイミングでイベントを発行する */
@Component
public class SignupEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	
	public SignupEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
	
	public void publishSignupEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
	}
}
