package com.example.nagoyameshi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.event.SignupEventPublisher;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.service.UserService;
import com.example.nagoyameshi.service.VerificationTokenService;

import jakarta.servlet.http.HttpServletRequest;



/* @Controller	: クラスがコントローラーとして機能する
 * @GetMapping	：　サーバーから情報を取得 */
@Controller
public class AuthController {
	private final UserService userService;
	private final SignupEventPublisher signupEventPublisher;
	private final VerificationTokenService verificationTokenService;
	
	public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService) {
		this.userService = userService;
		this.signupEventPublisher = signupEventPublisher;
		this.verificationTokenService = verificationTokenService;
	}
	
	@GetMapping("/login")
	public String login() {
		//System.out.println("aaa");	//breakpoint
		return "auth/login";
	}
	
	/* フォームクラスのインスタンスをModelクラスを使ってコントローラからビューに渡す
	 * addAttributeを使用し、参照する変数名と渡すデータを指定 
	 * auth/signup.htmlでsignupForm(変数名)を使用すると「new SignupForm」の戻り値を参照 */
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "auth/signup";
	}
	
	/* フォームの入力内容をHTTPリクエストのPOSTメソッドで送信するためPostMappingを付ける
	 * ModelAttribute　メソッドの引数につけるとフォームから送信されたデータをその引数にバインドする
	 * Validated　引数につけるとその引数に対しバリデーションを行う
	 * 　　　　　　　　エラーが存在する場合はBindingResultに格納される
	 * BindingResult　バリデーションの結果を保持するインターフェース　引数にバリデーションのエラー内容が格納される
	 * 				　hasErrors()でエラーの存在をチェックする（signup.htmlで設定したth:errors属性に値が格納される）
	 * 				 addError()にエラー内容を渡しBindingResultオブジェクトに独自のエラー内容を追加する
	 * 					FieldErrorクラスのインスタンスを作成した場合の引数
	 * 					FieldError(エラー内容を格納するオブジェクト名, エラー内容を発生させるフィールド名, エラーメッセージ)
	 * createUser()　サービスクラスに定義したcreateUser()メソッドを実行＆フォームクラスのインスタンスを渡し会員登録を行う
	 * 会員登録に成功すればトップページにリダイレクトし、メッセージを表示させる
	 * HttpServletRequest メール認証用のURLのドメイン名がローカル環境・本番環境で異なる・本番環境を移動する際に変更される可能性があるため動的にURLを取得 */
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute @Validated SignupForm signupForm,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 HttpServletRequest httpServletRequest,
						 Model model)
	{
		// メールアドレスが登録済みであればBindingResultオブジェクトにエラー内容を追加する
		if (userService.isEmailRegistered(signupForm.getEmail())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "既に登録済のメールアドレスです");
			bindingResult.addError(fieldError);
		}
		
		// パスワードとパスワード(確認用)の入力値が一致しなければ、BindingResultオブジェクトにエラー内容を追加する
		if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
			FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません");
			bindingResult.addError(fieldError);
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("signupForm", signupForm);
			
			return "auth/signup";
		}
		
		User createdUser = userService.CreateUser(signupForm);
		String requestUrl = new String(httpServletRequest.getRequestURL());	// リクエストURLを取得
		signupEventPublisher.publishSignupEvent(createdUser, requestUrl);	// ユーザーの会員登録が完了するとイベント発行
		redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");
		
		return "redirect:/";	// マイページにリダイレクトする
	}
	
	/* @RequestParam　リクエストパラメータ（HTTPリクエストに含まれるデータ）を引数にバインドする
	 * 		name　リクエストパラメータ名
	 * 		required　リクエストパラメータが必須か（デフォルトtrue）
	 * 		defaultValue　リクエストパラメータの値が未指定/空の場合のデフォルト値
	 * 													の属性を指定できる
	 * 	if文　RequestParamで取得したトークンがデータベースに存在するか検索
	 * 		　存在すれば会員を有効にし、メッセージを表示
	 * 		　存在しなければelseの内容のメッセージを表示
	 */
	@GetMapping("/signup/verify")
	public String verify(@RequestParam(name = "token") String token, Model model) {
		VerificationToken verificationToken = verificationTokenService.findVerificationTokenByToken(token);
		
		if (verificationToken != null) {
			User user = verificationToken.getUser();
			userService.enableUser(user);
			String successMessage = "会員登録が完了しました";
			model.addAttribute("successMessage", successMessage);
		} else {
			String errorMessage = "トークンが無効です";
			model.addAttribute("erroeMessage", errorMessage);
		}
		
		return "auth/verify";
	}
	
}
