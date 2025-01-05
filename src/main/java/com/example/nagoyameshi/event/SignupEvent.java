package com.example.nagoyameshi.event;
 
import org.springframework.context.ApplicationEvent;

import com.example.nagoyameshi.entity.User;

import lombok.Getter;

/* Eventクラス　イベント（特定の処理により発生するアクション）の発生をListenerクラスに知らせる
 * ApplicationEventクラス（イベント作成の基本的なクラスで、ソース（発生源）を保持）を継承して使用
 * @Getter 外部（Listenerクラス）から情報を取得するため定義する
 * 		SugnupEvent()　superで親クラスのコンストラクタを呼び出し、イベントのソースを渡す
 * 		ユーザーの情報とリクエストを受けたURLを保持する
 */
@Getter
public class SignupEvent extends ApplicationEvent {
	private User user;
	private String requestUrl;
	
	public SignupEvent(Object source, User user, String requestUrl) {
		super(source);
		
		this.user = user;
		this.requestUrl = requestUrl;
	}
}
