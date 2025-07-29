package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;


public class GuitaHero {
    // 键盘布局字符串，从最低音到最高音
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    // 基准音高 A4，频率为 440 Hz
    public static final double CONCERT_A = 440.0;

    public static void main(String[] args) {
        // 创建一个 GuitarString 数组，其大小与键盘上的键数相同
        GuitarString[] strings = new GuitarString[KEYBOARD.length()];

        // 初始化数组中的每一个 GuitarString
        for (int i = 0; i < KEYBOARD.length(); i++) {
            // 计算第 i 个琴弦的频率
            // 公式：440 * 2^((i - 24) / 12)
            double frequency = CONCERT_A * Math.pow(2, (i - 24.0) / 12.0);
            strings[i] = new GuitarString(frequency);
        }

        // 程序主循环，无限运行
        while (true) {

            // 检查是否有按键事件
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();

                // 查找按下的键对应的索引
                int index = KEYBOARD.indexOf(key);

                // 如果按键有效 (在我们的键盘布局中)
                if (index != -1) {
                    // 拨动相应的琴弦，使其发声
                    strings[index].pluck();
                }
            }

            // --- 以下代码在每次循环迭代中都会执行 ---

            // 计算当前时刻所有琴弦声音的叠加值
            double sample = 0.0;
            for (GuitarString s : strings) {
                sample += s.sample();
            }

            // 播放叠加后的声音
            StdAudio.play(sample);

            // 更新所有琴弦的状态，为下一个时间点做准备
            for (GuitarString s : strings) {
                s.tic();
            }
        }
    }
}
