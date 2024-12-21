package com.example.nagoyameshi.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.VerificationTokenService;

/* @Component ListenerクラスのインスタンスがDIコンテナに登録されるようにする */
@Component
public class SignupEventListener {
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	/* @EventListener　イベント発生時に実行したいメソッドに対しつける
	 * 		メソッドの引数に「どのイベントの発生時か」通知を受けるEventクラスを設定
	 * UUID（ほぼ重複しない一意のID）　トークンを生成
	 * 		生成したトークンをメール認証用のURLにパラメータとして埋め込む
	 * 		"/verify?token="
	 * SimpleMailMessageクラス　シンプルなメッセージをオブジェクトとして作成できる
	 * 		setFrom()	　送信元のメールアドレスをセット
	 * 		setTo()		　送信先のメールアドレスをセット
	 * 		setSubject()　件名
	 * 		setText()	　本文
	 * javaMailSenderインターフェース　.send()でSimpleMailMessageオブジェクトを渡してメールを送信する処理 */
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		User user = signupEvent.getUser();
		String token = UUID.randomUUID().toString();
		verificationTokenService.cretate(user, token);
		
		String senderAddress = "pocohco1013@gmail.com";
		String recipientAddress = user.getEmail();
		String subject = "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックして会員登録を完了してください。";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
	}
}
