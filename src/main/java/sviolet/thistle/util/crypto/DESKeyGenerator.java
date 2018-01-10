/*
 * Copyright (C) 2015-2017 S.Violet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project GitHub: https://github.com/shepherdviolet/thistle
 * Email: shepherdviolet@163.com
 */

package sviolet.thistle.util.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * DES秘钥生成工具
 */
public class DESKeyGenerator {

	public static final String DES_KEY_ALGORITHM = "DES";
	public static final String DES_EDE_KEY_ALGORITHM = "DESede";

	/**
	 * <p>生成64(56)位DES密钥, 用于服务端场合</p>
	 *
	 * @param secureRandom SecureRandom是线程安全的, 服务端通常使用一个单例的SecureRandom
	 * @return 秘钥
	 */
	public static byte[] generateDes64(SecureRandom secureRandom) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置56但是出来的是64bits
		return BaseKeyGenerator.generateKey(secureRandom, 56, DES_KEY_ALGORITHM);
	}

	/**
	 * <p>生成128(112)位DESede密钥, 用于服务端场合</p>
	 *
	 * @param secureRandom SecureRandom是线程安全的, 服务端通常使用一个单例的SecureRandom
	 * @return 秘钥
	 */
	public static byte[] generateDesEde128(SecureRandom secureRandom) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置112但是出来的是128bits
		return BaseKeyGenerator.generateKey(secureRandom, 112, DES_EDE_KEY_ALGORITHM);
	}

	/**
	 * <p>生成192(168)位DESede密钥, 用于服务端场合</p>
	 *
	 * @param secureRandom SecureRandom是线程安全的, 服务端通常使用一个单例的SecureRandom
	 * @return 秘钥
	 */
	public static byte[] generateDesEde192(SecureRandom secureRandom) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置168但是出来的是192bits
		return BaseKeyGenerator.generateKey(secureRandom, 168, DES_EDE_KEY_ALGORITHM);
	}

	/**
	 * <p>生成64(56)位DES密钥(不同系统平台相同seed生成结果可能不同), Android使用该方法, 相同seed仍会产生随机秘钥</p>
	 *
	 * @param seed 秘钥种子
	 * @return 秘钥
	 */
	public static byte[] generateDes64(byte[] seed) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置56但是出来的是64bits
		return BaseKeyGenerator.generateKey(seed, 56, DES_KEY_ALGORITHM);
	}

	/**
	 * <p>生成128(112)位DESede密钥(不同系统平台相同seed生成结果可能不同), Android使用该方法, 相同seed仍会产生随机秘钥</p>
	 *
	 * @param seed 秘钥种子
	 * @return 秘钥
	 */
	public static byte[] generateDesEde128(byte[] seed) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置112但是出来的是128bits
		return BaseKeyGenerator.generateKey(seed, 112, DES_EDE_KEY_ALGORITHM);
	}

	/**
	 * <p>生成192(168)位DESede密钥(不同系统平台相同seed生成结果可能不同), Android使用该方法, 相同seed仍会产生随机秘钥</p>
	 *
	 * @param seed 秘钥种子
	 * @return 秘钥
	 */
	public static byte[] generateDesEde192(byte[] seed) throws NoSuchProviderException, NoSuchAlgorithmException {
		//这里配置168但是出来的是192bits
		return BaseKeyGenerator.generateKey(seed, 168, DES_EDE_KEY_ALGORITHM);
	}

	/**
	 * 利用SHA256摘要算法计算64位固定密钥, 安全性低, 但保证全平台一致
	 *
	 * @param seed 密码种子
	 */
	public static byte[] generateShaKey64(byte[] seed){
		return BaseKeyGenerator.generateShaKey64(seed);
	}

	/**
	 * 利用SHA256摘要算法计算192位固定密钥, 安全性低, 但保证全平台一致
	 *
	 * @param seed 密码种子
	 */
	public static byte[] generateShaKey192(byte[] seed){
		return BaseKeyGenerator.generateShaKey192(seed);
	}

}
