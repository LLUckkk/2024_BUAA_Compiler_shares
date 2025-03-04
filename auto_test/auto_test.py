import os
import subprocess

# 配置你的编译器和 MARS 位置
COMPILER_PATH = r"hw7_optimize.jar"  # 替换为你的编译器 JAR 文件路径
MARS_PATH = r"MARS-2024.jar"  # 替换为 MARS JAR 文件的路径
TEST_DIR = r"testcases"  # 替换为存放测试用例的文件夹路径
MIPS_FILE = "mips.txt"  # 编译器固定生成的 MIPS 汇编文件名

def run_test_case(test_case_dir):
    testfile_path = os.path.join(test_case_dir, "testfile.txt")  # 测试文件
    input_path = os.path.join(test_case_dir, "in.txt")  # 输入文件
    expected_output_path = os.path.join(test_case_dir, "ans.txt")  # 标准输出文件

    if not os.path.exists(testfile_path) or not os.path.exists(expected_output_path):
        print(f"[ERROR] {test_case_dir} 缺少 test.txt 或 ans.txt")
        return False

    # 1. 编译 testfile 生成 MIPS 汇编文件（mips.txt）
    compile_cmd = ["java", "-jar", COMPILER_PATH, testfile_path]
    result = subprocess.run(compile_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

    if result.returncode != 0:
        print(f"[ERROR] 编译失败: {test_case_dir}\n{result.stderr}")
        return False

    # 检查 mips.txt 是否生成
    if not os.path.exists(MIPS_FILE):
        print(f"[ERROR] 编译器未生成 {MIPS_FILE}")
        return False

    # 2. 运行 MIPS 汇编文件
    mars_cmd = ["java", "-jar", MARS_PATH, "nc", MIPS_FILE]

    # 读取输入文件（如果存在）
    if os.path.exists(input_path):
        with open(input_path, "r") as input_file:
            mars_result = subprocess.run(mars_cmd, stdin=input_file, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    else:
        mars_result = subprocess.run(mars_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)

    if mars_result.returncode != 0:
        print(f"[ERROR] 运行 MIPS 失败: {test_case_dir}\n{mars_result.stderr}")
        return False

    # 3. 对比输出
    with open(expected_output_path, "r") as expected_file:
        expected_output = expected_file.read().strip()

    actual_output = mars_result.stdout.strip()

    if actual_output == expected_output:
        print(f"[PASS] {os.path.basename(test_case_dir)}")
        return True
    else:
        print(f"[FAIL] {os.path.basename(test_case_dir)}")
        print(f"Expected:\n{expected_output}\n")
        print(f"Got:\n{actual_output}\n")
        return False

# 运行所有测试用例
def run_all_tests():
    test_cases = [os.path.join(TEST_DIR, d) for d in os.listdir(TEST_DIR) if os.path.isdir(os.path.join(TEST_DIR, d)) and d.startswith("testcase")]

    passed = 0
    total = len(test_cases)

    for test_case in sorted(test_cases, key=lambda x: int(x[len(TEST_DIR) + 9:])):  # 按 testcaseX 号码排序
        if run_test_case(test_case):
            passed += 1

    print(f"\n测试完成: {passed}/{total} 通过")

if __name__ == "__main__":
    run_all_tests()
