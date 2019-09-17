package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.utils.ObjectMapperUtil;
import com.jt.vo.EasyUI_Tree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper itemCatMapper;
	//操作集群
	@Autowired
	private JedisCluster jedisCluster;
	//操作哨兵
	//@Autowired	//从哨兵池中取
	//private Jedis jedis;
	//操作分片
	//@Autowired
	//private ShardedJedis jedis;
	//操作单台
	//@Autowired private Jedis jedis;
	@Override
	public String findItemCatNameById(Long itemCatId) {
		
		return itemCatMapper.selectById(itemCatId).getName();
	}
	/**
	 * List<EasyUI_Tree>返回的是vo对象集合
	 * List<ItemCat>返回ItemCat集合对象
	 */
	@Override
	public List<EasyUI_Tree> findItemCatByParentId(Long parentId) {
		List<EasyUI_Tree> treeList=new ArrayList<>();
		//1.获取数据库数据
		List<ItemCat> itemCatList=findItemCatList(parentId);
		for (ItemCat itemCat : itemCatList) {
			Long id=itemCat.getId();
			String text=itemCat.getName();
			//一二级目录关，三级开
			String state=itemCat.getIsParent()?"closed":"open";
			EasyUI_Tree tree=new EasyUI_Tree(id,text,state);
			treeList.add(tree);
		}
		
		return treeList;
	}
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}
	//查询缓存
	@Override
	public List<EasyUI_Tree> findItemCatByCache(Long parentId) {
		String key="ITEM_CAT_"+parentId;
		String result = jedisCluster.get(key);
		List<EasyUI_Tree> treeList=new ArrayList<>();
		if(StringUtils.isEmpty(result)) {
			treeList=findItemCatByParentId(parentId);//此处直接调用上面的方法
			//将对象转化为json
			String json = ObjectMapperUtil.toJson(treeList);
			//将json保存到缓存中
			jedisCluster.set(key, json);
			System.out.println("查询数据库");
		}else {
			//如果缓存中有数据,将json转化为对象
			treeList=ObjectMapperUtil.toObject(result,treeList.getClass() );
			System.out.println("查询缓存");
		}
		return treeList;
	}
}
