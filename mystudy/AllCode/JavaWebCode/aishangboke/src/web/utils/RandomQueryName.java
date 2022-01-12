package web.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @Classname RandomQueryName
 * @Description 点名器
 * @Date 2021/12/29 8:57
 * @Created by zhq
 */
public class RandomQueryName {
    private static int num = 0;

    public static void main(String[] args) throws IOException {
        List<String> names = new ArrayList<>();
        List<String> hasNames = new ArrayList<>();
        //读取所有人员
        BufferedReader fileReader = new BufferedReader(new FileReader("src/web/utils/name.txt"));
        while (fileReader.ready()) {
            names.add(fileReader.readLine());
        }
        //读取所有已展示人员
        BufferedReader hasFileReader = new BufferedReader(new FileReader("src/web/utils/hasname.txt"));
        while (hasFileReader.ready()) {
            hasNames.add(hasFileReader.readLine());
        }
        fileReader.close();
        hasFileReader.close();
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setSize(500, 500);
        JLabel label = new JLabel();
        label.setBounds(170, 80, 300, 300);
        label.setText("准备好了吗？");
        label.setFont(new Font("微软雅黑", Font.BOLD, 50));
        frame.add(label);
        JButton button = new JButton();
        button.setSize(150, 150);
        button.setFont(new Font("微软雅黑", Font.BOLD, 20));
        button.setText("随机展示");
        Set<Integer> hasName = new HashSet<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/web/utils/hasname.txt", true));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Random random = new Random();
                int i = random.nextInt(names.size());
                while (!hasName.add(i) || hasNames.contains(names.get(i))) {
                    i = random.nextInt(names.size());
                }
                RandomQueryName.num++;
                String value = names.get(i);
                label.setText(value);
                try {
                    writer.write(value + "\n");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //本轮结束
                if (RandomQueryName.num % 5 == 0) {
                    JOptionPane.showMessageDialog(null, "完毕");
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        frame.add(button);
        writer.flush();
    }
}
