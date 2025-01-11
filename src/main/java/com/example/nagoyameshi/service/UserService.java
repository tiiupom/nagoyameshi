package com.example.nagoyameshi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
	}
	
	/* Transactional　メソッドをトランザクション化（データベースの操作をひとまとまりにする）
	 * 	正常に終了すれば、トランザクションがコミットされデータベースの変更が保存される
	 * 	例外が発生すれば、ロールバックされデータベースの変更が破棄される
	 * 	１　エンティティをインスタンス化して引数で受け取ったフォームの内容を取得しセッターを使用してインスタンス化したエンティティの各フィールドにセット
	 * 	2　パスワードをハッシュ化してセット
	 * 	3　roleフィールドにRoleオブジェクトをセット（このフォームでは無料会員でのみ登録するためGENERALのみ設定）
	 * 	4　enabledフィールドにtrueをセットし、メール認証済みか判定する
	 * 	5　saveメソッドでエンティティをデータベースに保存 */
	
	@Transactional
	public User CreateUser(SignupForm signupForm) {
		User user = new User();
		Role role = roleRepository.findByName("ROLE_GENERAL");
		
		user.setName(signupForm.getName());
		user.setFurigana(signupForm.getFurigana());
		user.setPhoneNumber(signupForm.getPhoneNumber());
		user.setEmail(signupForm.getEmail());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.setRole(role);
		user.setEnabled(false);
		
		return userRepository.save(user);
	}
	
	// 会員情報の編集内容を取得し保存
	@Transactional
	public void updateUser(UserEditForm userEditForm, User user) {
		user.setName(userEditForm.getName());
		user.setFurigana(userEditForm.getFurigana());
		user.setPhoneNumber(userEditForm.getPhoneNumber());
		user.setEmail(userEditForm.getEmail());
		
		userRepository.save(user);
	}
	
	/* メールアドレスが登録済みかチェック
	   登録していなければfalseを返す */
	public boolean isEmailRegistered(String email) {
		User user = userRepository.findByEmail(email);
		return user != null;
	
	}
	
	/* パスワード・パスワード（確認用）の値が一致するかチェック
	 * equalsメソッドで文字列同士を比較し、trueかfalseを返す
	 * 比較結果の値をそのままreturnで返す */
	public boolean isSamePassword(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
	
	/*　一致するデータがあれば、データに紐づいたユーザーのenableカラムの値をtrueにする */
	@Transactional
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	// メールアドレスが変更されたかチェック
	public boolean isEmailChanged(UserEditForm userEditForm, User user) {
		return !userEditForm.getEmail().equals(user.getEmail());
	}
	
	// 指定したメールアドレスを持つユーザーを取得
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
