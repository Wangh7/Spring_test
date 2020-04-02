package com.wangh7.wht.service;

import com.wangh7.wht.dao.MenuDAO;
import com.wangh7.wht.pojo.Menu;
import com.wangh7.wht.pojo.RoleMenu;
import com.wangh7.wht.pojo.User;
import com.wangh7.wht.pojo.UserRole;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    MenuDAO menuDAO;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;

    public List<Menu> getAllByParentId(int parentId){
        return menuDAO.findAllByParentId(parentId);
    }

    public List<Menu> getMenusByCurrentUser() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);
        List<UserRole> userRoleList = userRoleService.listAllByUserId(user.getId());
        List<Menu> menus = new ArrayList<>();
        for(UserRole userRole : userRoleList) {
            List<RoleMenu> roleMenus = roleMenuService.findAllByRoleId(userRole.getRoleId());
            for(RoleMenu roleMenu : roleMenus) {
                //防止多角色菜单重复
                Menu menu = menuDAO.findById(roleMenu.getMenuId());
                boolean isExist = false;
                for (Menu menu1 : menus) {
                    if(menu1.getId() == menu.getId()){
                        isExist = true;
                    }
                }
                if (!isExist) {
                    menus.add(menu);
                }
            }
        }
        handleMenus(menus);
        return menus;
    }

    public List<Menu> getMenusByRoleId(int role_id){
        List<Menu> menus = new ArrayList<>();
        List<RoleMenu> roleMenus = roleMenuService.findAllByRoleId(role_id);
        for(RoleMenu roleMenu : roleMenus) {
            menus.add(menuDAO.findById(roleMenu.getMenuId()));
        }
        handleMenus(menus);
        return menus;
    }

    // 剔除子项，仅保留父项
    public void handleMenus(List<Menu> menus) {
        for(Menu menu : menus) {
            List<Menu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }
        menus.removeIf(menu -> menu.getParentId() != 0);
    }
}
