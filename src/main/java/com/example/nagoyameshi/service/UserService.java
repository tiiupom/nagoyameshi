package com.example.nagoyameshi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	// 全てのユーザーをページングされた状態で取得
	public Page<User> findAllUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	// 指定されたキーワードを氏名orフリガナに含むユーザーをページングされた状態で取得
	public Page<User> findUserByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable) {
		return userRepository.findByNameLikeOrFuriganaLike("%" + nameKeyword + "%", "%" + furiganaKeyword + "%", pageable);
	}
	
	// 指定されたIDを持つユーザーを取得
	public Optional<User> findUserById(Integer id) {
		return userRepository.findById(id);
	}
	
	@Transactional
	public void saveStripeCustomerId(User user, String stripeCustomerId) {
		user.setStripeCustomerId(stripeCustomerId);
		userRepository.save(user);
	}
	
	@Transactional
	public void updateRole(User user, String roleName) {
		Role role = roleRepository.findByName(roleName);
		user.setRole(role);
		userRepository.save(user);
	}
	
	// 認証情報のロールを更新
	public void refreshAuthenticationByRole(String newRole) {
		Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 新しい認証情報を作成
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
		simpleGrantedAuthorities.add(new SimpleGrantedAuthority(newRole));
		Authentication newAuthentication = new UsernamePasswordAuthenticationToken(currentAuthentication.getPrincipal(), currentAuthentication.getCredentials(), simpleGrantedAuthorities);
		
		// 認証情報を更新
		SecurityContextHolder.getContext().setAuthentication(newAuthentication);
	}
	
	
}
