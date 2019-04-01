package 文本编辑器;

import java.util.HashMap;

public class 提示词典 {

  private static final HashMap<String, String[]> 提示词典 = new HashMap<>();
  static {
    提示词典.put("xj", new String[] {"新建"});
    提示词典.put("dk", new String[] {"打开"});
    提示词典.put("bc", new String[] {"保存"});
    提示词典.put("jq", new String[] {"剪切"});
    提示词典.put("fz", new String[] {"复制"});
    提示词典.put("nt", new String[] {"粘贴"});
    提示词典.put("zt", new String[] {"粘贴"});
    提示词典.put("tc", new String[] {"退出"});
    提示词典.put("j", new String[] {"新建", "剪切"});
    提示词典.put("t", new String[] {"退出", "粘贴"});
  }

  public static String[] 取提示(String 源词) {
    return 提示词典.get(源词);
  }
}
