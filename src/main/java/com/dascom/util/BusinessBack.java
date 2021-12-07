package com.dascom.util;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*
* @author crt
* 
*/
@Setter
@Getter
@ToString
public class BusinessBack {
	
	/**
	 * 调用结果，0返回成功数据，非0返回失败数据
	 */
	private int code;
	
	/**
	 * 业务数据
	 */
	private Object data;
	
	/**
	 * 构造私有
	 */
    private BusinessBack (){}  

    /**
     * 成功返回
     * @param data
     * @return
     */
	public static BusinessBack success(Object data) {
		BusinessBack businessBack=new BusinessBack();
		businessBack.setData(data);
		businessBack.setCode(0);
        return businessBack;
    }

    /**
     * 失败返回
     * @param errorMsg
     * @return
     */
    public static BusinessBack failed(int code,Object data) {
    	BusinessBack businessBack=new BusinessBack();
    	businessBack.setData(data);
    	businessBack.setCode(code);;
        return businessBack;
    }
}
