<template>
  <div class="container">
    <!-- 注册表单容器 -->
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎注册</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <!-- 用户名输入框 -->
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <!-- 密码输入框 -->
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password v-model="form.password"></el-input>
        </el-form-item>
        <!-- 确认密码输入框 -->
        <el-form-item prop="confirmPass">
          <el-input prefix-icon="el-icon-lock" placeholder="请确认密码" show-password v-model="form.confirmPass"></el-input>
        </el-form-item>
        <!-- 注册按钮 -->
        <el-form-item>
          <el-button style="width: 100%; background-color: #333; border-color: #333; color: white" @click="register">注
            册</el-button>
        </el-form-item>
        <!-- 登录链接 -->
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            已有账号？请 <a href="/login">登录</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register", // 组件名称
  data() {
    // 自定义验证函数，校验确认密码是否与密码一致
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码')) // 确认密码为空时提示
      } else if (confirmPass !== this.form.password) {
        callback(new Error('两次输入的密码不一致')) // 两次密码不一致时提示
      } else {
        callback() // 验证通过
      }
    }
    return {
      form: {}, // 表单数据
      rules: { // 表单验证规则
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }, // 用户名必填验证
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }, // 密码必填验证
        ],
        confirmPass: [
          { validator: validatePassword, trigger: 'blur' } // 自定义验证
        ]
      }
    }
  },
  created() {
    // 组件创建时的生命周期钩子，可用于初始化数据
  },
  methods: {
    register() {
      // 验证表单内容
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过后发送注册请求
          this.$request.post('/register', this.form).then(res => {
            if (res.code === '200') {
              this.$router.push('/') // 注册成功后跳转到首页
              this.$message.success('注册成功') // 显示成功提示
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
  background-image: url("@/assets/imgs/bg1.jpg");
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
