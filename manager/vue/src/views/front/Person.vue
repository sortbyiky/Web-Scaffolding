<template>
  <div class="main-content">
    <!-- 使用 Element UI 的 Card 组件 -->
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <!-- 修改密码按钮，点击时触发 updatePassword 方法 -->
        <el-button type="primary" @click="updatePassword">修改密码</el-button>
      </div>
      <!-- 表单，用于展示和编辑用户信息 -->
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <!-- 头像上传区域 -->
        <div style="margin: 15px; text-align: center">
          <el-upload class="avatar-uploader" :action="$baseUrl + '/files/upload'" <!-- 上传接口地址 -->
            :show-file-list="false" <!-- 不显示文件列表 -->
            :on-success="handleAvatarSuccess" <!-- 上传成功时的回调 -->
            >
            <!-- 如果有头像则显示头像，否则显示加号图标 -->
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <!-- 用户名显示 -->
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <!-- 姓名输入框 -->
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <!-- 电话输入框 -->
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <!-- 邮箱输入框 -->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <!-- 保存按钮 -->
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 修改密码对话框 -->
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <!-- 原始密码输入框 -->
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <!-- 新密码输入框 -->
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <!-- 确认密码输入框 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <!-- 取消按钮 -->
        <el-button @click="dialogVisible = false">取 消</el-button>
        <!-- 确定按钮 -->
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    // 自定义验证函数，检查确认密码是否与新密码一致
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'), // 从本地存储中获取用户信息
      dialogVisible: false,  // 控制修改密码对话框的显示状态

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
      // 保存当前的用户信息到数据库
      this.$request.put('/admin/update', this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把 user 的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
    // 修改密码
    updatePassword() {
      this.dialogVisible = true // 打开修改密码对话框
    },
    save() {
      // 验证表单内容
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          // 提交修改密码请求
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login') // 修改成功后跳转到登录页面
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
  /* 设置表单标签字体加粗 */
}

/deep/.el-upload {
  border-radius: 50%;
  /* 设置上传组件的边框为圆形 */
}

/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  /* 设置上传组件的边框样式 */
  cursor: pointer;
  /* 设置鼠标指针为手型 */
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}

/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
  /* 设置上传组件悬停时的边框颜色 */
}

.avatar-uploader-icon {
  font-size: 28px;
  /* 设置上传图标的字体大小 */
  color: #8c939d;
  /* 设置上传图标的颜色 */
  width: 120px;
  /* 设置上传组件的宽度 */
  height: 120px;
  /* 设置上传组件的高度 */
  line-height: 120px;
  /* 设置行高 */
  text-align: center;
  /* 设置文本居中 */
  border-radius: 50%;
  /* 设置边框为圆形 */
}

.avatar {
  width: 120px;
  /* 设置头像图片的宽度 */
  height: 120px;
  /* 设置头像图片的高度 */
  display: block;
  /* 设置头像为块级元素 */
  border-radius: 50%;
  /* 设置头像为圆形 */
}
</style>
