package dogwalk.dao;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dogwalk.model.Dog;

public class DogDao {
	// 싱글톤
	private static DogDao instance = new DogDao();
	
	private DogDao() {}
	
	public static DogDao getInstance() {
		return instance;
	}
	
	// 마이베이티스
	private static SqlSession session;
	static {
		try {
			Reader reader = Resources.getResourceAsReader("configuration.xml");
			SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(reader);
			
			session = ssf.openSession(true);	// true: auto commit, 반드시 기술해야함
			reader.close();
		} catch (Exception e) {
			System.out.println("session 생성에러: "+e.getMessage());
		}
	}
	
	public List<Dog> list(int startRow, int endRow) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("startRow", startRow);
		hm.put("endRow", endRow);
		return session.selectList("dogns.list", hm);
	}

	public int total() {
		return (int) session.selectOne("dogns.total");
	}

	public Dog select(String dog_no) {
		return (Dog) session.selectOne("dogns.selectView", dog_no);
	}

	public List<Dog> searchlist(int startRow, int endRow, String type, String search) {
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("startRow", startRow);
		hm.put("endRow", endRow);
		hm.put("type", type);
		hm.put("search", search);
		return session.selectList("dogns.searchlist", hm);
	}
	
	// 견주 가입 - 반려견 테이블에 데이터 넣기 - LJH
	public int insert(Dog dog) {
		return session.insert("dogns.insertDog", dog);
	}
	// 견주 로그인 후 반려견 정보 조회 - LJH
	public Dog selectDog(String own_id) {
		return (Dog)session.selectOne("dogns.selectDogInfo", own_id);
	}
	// 견주 로그인 후 반려견 정보 업데이트 - LJH
	public int updateDog(Dog dog) {
		return session.update("dogns.updateDog", dog);
	}
	
}