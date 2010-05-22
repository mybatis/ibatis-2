package threads;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import com.ibatis.sqlmap.client.SqlMapClient;
import threads.Foo;

class MyThread extends Thread {

	private SqlMapClient sqlMap;
	private String remap;

	public MyThread(SqlMapClient sqlMap, String remap) {
		this.remap = remap;
		this.sqlMap = sqlMap;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			try {
				List<Foo> list = sqlMap.queryForList("selectFoo" + remap, null);
				TestCase.assertEquals(300, list.size());
				check(list);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void check(List<Foo> list) {
		for (Foo foo : list) {
			if (foo == null) {
				TestCase.fail("list contained a null element");
			}
		}
	}
}