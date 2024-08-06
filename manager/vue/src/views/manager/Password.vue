<template>
  <div>
    <!-- 使用 Element UI 的 Card 组件 -->
    <el-card style="width: 50%">
      <!-- 表单，用于输入和验证用户密码信息 -->
      <el-form ref="formRef" :model="user" :rules="rules" label-width="100px" style="padding-right: 50px">
        <!-- 原始密码输入框 -->
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <!-- 新密码输入框 -->
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <!-- 确认新密码输入框 -->
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <!-- 确认修改按钮 -->
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Password", // 组件名称
  data() {
    // 自定义验证函数，用于验证确认密码是否与新密码一致
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码')) // 如果确认密码为空，提示错误
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误')) // 如果确认密码不一致，提示错误
      } else {
        callback() // 验证通过
      }
    }

    return {
      // 从本地存储中获取当前用户信息
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      // 表单验证规则
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' }, // 验证原始密码
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' }, // 验证新密码
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' }, // 验证确认密码
        ],
      }
    }
  },
  created() {
    // 组件创建时的生命周期钩子，可用于初始化数据
  },
  methods: {
    update() {
      // 验证表单内容
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 发起修改密码请求
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              localStorage.removeItem('xm-user')   // 清除缓存的用户信息
              this.$message.success('修改密码成功') // 显示成功信息
              this.$router.push('/login') // 跳转到登录页面
            } else {
              this.$message.error(res.msg) // 显示错误信息
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
  /* 设置表单标签字体加粗 */
}
</style>
