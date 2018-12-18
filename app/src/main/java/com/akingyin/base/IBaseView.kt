package com.akingyin.base

/**
 * @ Description:
 * @author king
 * @ Date 2018/8/3 15:57
 * @version V1.0
 */
interface IBaseView  {


    /**
     * 显示消息
     * @param msg
     */
    fun showMessage(msg: String?)

    /**
     * 显示成功提示信息
     * @param msg
     */
    fun showSucces(msg: String?)

    /**
     * 显示错误提示信息
     * @param msg
     */
    fun showError(msg: String?)

    fun showWarning(msg: String?)

    fun close()

    fun showTips(msg: String?)

    /**
     * 显示等待对话框
     * @param msg
     */
    fun showLoadDialog(msg: String?)

    /**
     * 隐藏对话框
     */
    fun hideLoadDialog()



    fun showLoading()

    fun dismissLoading()
}