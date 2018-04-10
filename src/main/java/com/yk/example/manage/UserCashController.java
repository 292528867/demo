package com.yk.example.manage;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.yk.example.config.AliConfig;
import com.yk.example.entity.UserCash;
import com.yk.example.service.UserCashService;
import com.yk.example.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by yk on 2018/4/2.
 */
@ApiIgnore
@Controller
public class UserCashController {

    @Autowired
    private UserCashService userCashService;

    @Autowired
    private AliConfig aliConfig;

    /**
     * @param model
     * @param userCash
     * @param pageCurrent
     * @param pageSize
     * @param pageCount
     * @return
     */
    @RequestMapping(value = "/admin/userCashList_{pageCurrent}_{pageSize}_{pageCount}", method = RequestMethod.GET)
    public String index(Model model, UserCash userCash, @PathVariable Integer pageCurrent, @PathVariable Integer pageSize, @PathVariable Integer pageCount) {
        if (pageCurrent == 0) {
            pageCurrent = 1;
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        Page<UserCash> userCashPage = userCashService.findAll(userCash, new PageRequest(pageCurrent - 1, pageSize));
        model.addAttribute("userCashList", userCashPage.getContent());
        String pageHTML = PageUtils.getPageContent("userCashList_{pageCurrent}_{pageSize}_{pageCount}?auditStatus=" + userCash.getAuditStatus(), pageCurrent, pageSize, userCashPage.getTotalPages());
        model.addAttribute("pageHTML", pageHTML);
        model.addAttribute("userCash", userCash);
        return "user/userCashManage";
    }

    /**
     * 同意提现
     *
     * @return
     */
    @RequestMapping(value = "/admin/agreeCash", method = RequestMethod.POST)
    @ResponseBody
    public String agreeCash() {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", aliConfig.getAPP_ID(),
                aliConfig.getAPP_PRIVATE_KEY(), "json", "GBK", aliConfig.getALIPAY_PUBLIC_KEY(), aliConfig.getSignType());
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent("{" +
                "\"out_biz_no\":\"3142321423432\"," +
                "\"payee_type\":\"ALIPAY_LOGONID\"," +
                "\"payee_account\":\"abc@sina.com\"," +
                "\"amount\":\"12.23\"," +
                "\"payer_show_name\":\"上海交通卡退款\"," +
                "\"payee_real_name\":\"张三\"," +
                "\"remark\":\"转账备注\"" +
                "}");
        AlipayFundTransToaccountTransferResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return "";
    }
}
