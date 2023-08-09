<template>
  <!-- 登录的弹窗 -->
  <el-dialog title="" width="600px" :visible="isTrue" @close="$emit('close')">
    <div style="margin: 0 auto;width: 500px">
      <div class="boxs">
        <button class="login-box-sty" @click="loginIsShow=true">登录</button>
        <button class="login-box-sty" @click="loginIsShow=false">注册</button>
      </div>
      <div v-show="loginIsShow">
        <el-form :model="login" status-icon :rules="loginRules" ref="login" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="login.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="login.password" autocomplete="off"></el-input>
          </el-form-item>
          <div class="btnBox">
            <el-button type="primary" @click="loginForm('login')">登录</el-button>
            <el-button @click="resForm('login')">重置</el-button>
          </div>
        </el-form>
      </div>
      <div v-show="!loginIsShow">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="ruleForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPassword">
            <el-input type="password" v-model="ruleForm.checkPassword" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="昵称" prop="nickname">
            <el-input type="text" v-model="ruleForm.nickname" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input type="text" v-model="ruleForm.email" autocomplete="off"></el-input>
          </el-form-item>

          <!--获取验证码-->
          <el-form-item prop="code">
            <div class="bind_code margin_top">
              <el-input
                  class="bind_code_input code"
                  v-model="ruleForm.code"
                  type="text"
                  placeholder="请输入验证码"
              />
              <el-button
                  @click.native.prevent="bindforgetSendCode('ruleForm')"
                  class="bind_code_gain"
                  :disabled="disabled"
              >{{ btnText }}
              </el-button>
            </div>
          </el-form-item>
          <div class="btnBox">
            <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </div>
        </el-form>
      </div>
    </div>
    <!--    <el-tabs style="margin:0 auto" type="border-card">-->
    <!--      <el-tab-pane style="height: 450px" label="登录">-->
    <!--        -->
    <!--      </el-tab-pane>-->
    <!--      <el-tab-pane style="height: 450px" label="注册">-->
    <!--        -->
    <!--      </el-tab-pane>-->
    <!--    </el-tabs>-->
  </el-dialog>
</template>

<script>
import {getMe, login, register, vaildEmail,} from "@/utils/option";

export default {
  name: "login",
  props: {
    isTrue: Boolean,
  },
  data() {
    var validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPassword !== '') {
          this.$refs.ruleForm.validateField('checkPassword');
        }
        callback();
      }
    };
    var validatePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      loginIsShow: true, // 登录默认开启
      disabled: false,
      btnText: "发送验证码",
      dialogVisible: true,
      login: {
        username: '', // 登录要输入的用户名
        password: ''  // 登录要输入的密码
      },
      ruleForm: {
        username: '', // 用户名
        password: '',   // 密码
        checkPassword: '', // 再次输入密码
        nickname: '', // 昵称
        code: '', // 验证码
        email: '', // 邮箱
      },
      message: "",
      // 验证规则
      loginRules: {
        username: [
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: 'blur'}
        ]
      },
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {validator: validatePassword, trigger: 'blur'},
          {required: true}
        ],
        checkPassword: [
          {validator: validatePassword2, trigger: 'blur'},
          {required: true}
        ],
        code: [
          {required: true, message: "请输入短信验证码", trigger: "blur"},
        ],
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
        ],
        nickname: [
          {required: true, message: "请输入昵称", trigger: "blur"},
        ],
      },
    }
  },
  methods: {
    // 提交按钮
    //forName 当用户输入为空时,会提示用户名和密码不能为空
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 注册
          register({...this.ruleForm, sessionId: this.message}).then(res => {
            if (res.success) {
              this.$message.success(res.message)
              this.$emit('close');
            } else {
              this.$message.error(res.message)
            }
          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 重置按钮
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 登录按钮
    loginForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 登录成功之后给弹窗隐藏
          login(this.login).then(res => {
            if (res.success) {
              this.$notify({
                title: '成功',
                message: '登录成功!',
                type: 'success',
                position: 'bottom-right',
              });
              // 关闭弹窗
              this.$emit('close');

              if (res.data.token) {

                window.localStorage.setItem("youbiliToken", JSON.stringify(res.data.token));
                if (window.localStorage.getItem("youbiliToken")) {
                  getMe().then(res => {
                    if (res.success) {
                      this.$emit('showAvatar', {
                        userData: res.data
                      });
                    }
                  })
                }
              }
            } else {
              this.$notify.error({
                title: '错误',
                message: res.message,
                position: 'bottom-right',
              });
            }
          })
        } else {
          console.log('error login!!');
          return false;
        }
      });
    },
    // 重置按钮
    resForm(formName) {
      this.$refs[formName].resetFields();
    },

    // 发送验证码
    bindforgetSendCode(formName) {
      //手机号 为空的话
      this.$refs[formName].validateField("email", (valid) => {
        if (!valid) {
          this.$message("已发送验证码，请查收");
          this.disabled = true;
          this.btnText = "请稍候...";
          setTimeout(() => {
            this.doLoop(60);
          }, 500);
          // 发送验证码请求
          vaildEmail({email: this.ruleForm.email}).then(res => {
            if (res.status == 200) {
              this.$message.success("邮件已发送");
              this.message = res.message;
            } else {
              this.$message.error(res.message);
            }
          })
        } else {
          return false;
        }
      });
    },
    // 手机验证码的倒计时
    doLoop: function (seconds) {
      seconds = seconds ? seconds : 60;
      this.btnText = seconds + "s后获取";
      // this.code = code
      let countdown = setInterval(() => {
        if (seconds > 0) {
          this.btnText = seconds + "s后获取";
          --seconds;
        } else {
          this.btnText = "获取验证码";
          this.disabled = false;
          clearInterval(countdown);
        }
      }, 1000);
    },

  }
}
</script>

<style scoped>
.btnBox {
  margin-left: 100px;
  display: flex;
  justify-content: space-between;
}

.btnBox button {
  width: 190px;
}

.bind_code {
  position: relative;
}

.boxs {
  width: 300px;
  margin: 30px auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login-box-sty {
  border: 0;
  background-color: transparent;
  font-size: 20px;
  text-align: center;
  line-height: 40px;
  width: 120px;
  cursor: pointer;
}

.login-box-sty:hover {
  color: #20aee5;
}

.bind_code .el-input__inner {
  width: 328px;
  height: 44px;
  background: #f7f7f7;
  border-radius: 4px;
  border: 1px solid #e4e4e4;
  outline: none;
  padding: 0 100px 0 10px;
}

.bind_code .code /deep/ .el-input__suffix {
  right: 97px;
}

.bind_code .el-button {
  border: 0;
  background: none;
  padding: 0;
  border-radius: 0;
}

.bind_code .pic {
  width: 80px;
  height: 25px;
  border-left: none !important;
}

.bind_code .bind_code_gain {
  position: absolute;
  top: calc(50% - 9px);
  right: 10px;
  font-size: 14px;
  font-family: MicrosoftYaHei;
  color: #20aee5;
  line-height: 18px;
  cursor: pointer;
  padding-left: 10px;
  border-left: 1px solid #d8d8d8;
}

.bind_code .login-icon {
  width: 16px;
  height: 16px;
  position: absolute;
  top: 14px;
  left: 16px;
}

</style>