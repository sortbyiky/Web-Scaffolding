<template>
  <div class="container">
    <!-- 登录表单容器 -->
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎使用</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <!-- 用户名输入框 -->
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password v-model="form.password"></el-input>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item>
          <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="login">登
            录</el-button>
        </el-form-item>
        <!-- 注册链接（已注释，可以根据需要启用） -->
        <!-- <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div> -->
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login", // 组件名称
  data() {
    return {
      form: { role: 'ADMIN' }, // 表单数据，默认为 ADMIN 角色
      rules: { // 表单验证规则
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }, // 用户名必填验证
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }, // 密码必填验证
        ]
      }
    }
  },
  created() {
    // 组件创建时的生命周期钩子，可用于初始化数据
  },
  methods: {
    login() {
      // 验证表单内容
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过后发送登录请求
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') { // 登录成功
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据到本地存储
              this.$router.push('/')  // 跳转到主页
              this.$message.success('登录成功') // 显示成功提示
            } else {
              this.$message.error(res.msg) // 显示错误信息
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
/* 样式定义 */
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
  /* 背景图片 */
  background-size: 100%;
  /* 背景尺寸 */
  display: flex;
  /* 使用 Flexbox 布局 */
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
  color: #666;
  /* 默认字体颜色 */
}

a {
  color: #2a60c9;
  /* 链接颜色 */
}
</style>
