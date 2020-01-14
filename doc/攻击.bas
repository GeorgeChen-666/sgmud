#==============================================================================
# ■ Game_Battler (分割定义 3)
#------------------------------------------------------------------------------
# 　处理战斗者的类。这个类作为 Game_Actor 类与 Game_Enemy 类的
# 超级类来使用。
#==============================================================================

class Game_Battler
  #--------------------------------------------------------------------------
  # ● 可以使用特技的判定
  #     skill_id : 特技 ID
  #--------------------------------------------------------------------------
  def skill_can_use?(skill_id)
    # SP 不足的情况下不能使用
    if $data_skills[skill_id].sp_cost > self.sp
      return false
    end
    # 战斗不能的情况下不能使用
    if dead?
      return false
    end
    # 沉默状态的情况下、物理特技以外的特技不能使用
    if $data_skills[skill_id].atk_f == 0 and self.restriction == 1
      return false
    end
    # 获取可以使用的时机
    occasion = $data_skills[skill_id].occasion
    # 战斗中的情况下
    if $game_temp.in_battle
      # [平时] 或者是 [战斗中] 可以使用
      return (occasion == 0 or occasion == 1)
    # 不是战斗中的情况下
    else
      # [平时] 或者是 [菜单中] 可以使用
      return (occasion == 0 or occasion == 2)
    end
  end
  #--------------------------------------------------------------------------
  # ● 应用通常攻击效果
  #     attacker : 攻击者 (battler) s_atk1:技能攻击力 s_atk2：技能机率内伤
  #--------------------------------------------------------------------------
  def attack_effect(attacker,s_atk1,s_atk2)
    # 清除会心一击标志
    self.critical = false
    # 第一命中判定
    hit_result = (rand(100) < attacker.hit)#命中率
    hits=attacker.dex*3+attacker.str*7
    hits/=10
    precent=100*(hits-self.dex)/self.dex
    n_hit=[precent,-30].max
    if attacker.is_a?(Game_Actor)
      n_hit+=(50+rand(13))
    else
      n_hit+=(40+rand(10))
    end
    hit_result &= (rand(100)<=n_hit)
    if hit_result == true
      # 计算基本伤害
      atk = [attacker.atk*10 - self.pdef, 0].max
      self.damage = atk/10 + s_atk1.to_i
      # 属性修正
      self.damage *= elements_correct(attacker.element_set)
      self.damage /= 100
      # 分散
      if self.damage.abs > 0
        amp = rand(1000)+9500.0
        self.damage *= amp/10000.0
      end
      self.damage=self.damage.truncate 
    end
    # 命中的情况下
    if hit_result == true
      if attacker.is_a?(Game_Actor)
        attacker.sp-=$game_variables[23]
        if attacker.sp<=0
          self.damage-=$game_variables[23]/2
          attacker.sp=0
        end
      end
      # 状态冲击解除
      remove_states_shock
      if self.damage<self.maxhp/30
        $defend_text=draw_defend2_text(self)
        hit_result == false
        self.damage=0
      elsif attacker.is_a?(Game_Actor)
        if attacker.weapon_id !=0
          if weapon_matching_skill?
            sk_id=$game_variables[31]
          else
            sk_id=get_weapon_base_skill
          end
        else
          sk_id=$game_variables[29]!=0 ? $game_variables[29]:40
        end
        result_array=draw_hit_result(self.name,sk_id)
        index=[result_array.size*self.damage/self.maxhp,result_array.size-1].min
        $defend_text=result_array[index]
      else
        result_array=draw_hit_result("你",$enemys_skills[0][0])
        index=[result_array.size*self.damage/self.maxhp,result_array.size-1].min
        $defend_text=result_array[index]
      end
      # HP 的伤害计算
      self.hp -= self.damage
      if self.is_a?(Game_Actor)
        if rand(100)<s_atk2.to_i
          self.maxhp-=[rand(self.damage*1.3),self.damage].min
          self.maxhp=[self.maxhp,1].max
        end
      end
      # 状态变化
      @state_changed = false
      states_plus(attacker.plus_state_set)
      states_minus(attacker.minus_state_set)
    # Miss 的情况下
    else
      $defend_text=draw_defend_text(self,attacker)
      # 伤害设置为 "Miss"
      self.damage = "Miss"
      # 清除会心一击标志
      self.critical = false
    end
    # 过程结束
    return true
  end
  #--------------------------------------------------------------------------
  # ● 应用特技效果
  #     user  : 特技的使用者 (battler)
  #     skill : 特技
  #--------------------------------------------------------------------------
  def skill_effect(user, skill ,window)
    words=get_skill_words(user ,self ,skill.id)
    if words[0].is_a?(Array)
      for i in words[0]
        window.set_text(i)
        for i in 0..20
          Graphics.update
        end
      end
    else
      window.set_text(words[0])
      for i in 0..20
        Graphics.update
      end
    end
    if skill.scope>=3
      states_plus(skill.plus_state_set)
      states_minus(skill.minus_state_set)
      self.damage = ""
      $defend_text=""
      return true
    end
    # 清除会心一击标志
    self.critical = false
    # 清除有效标志
    effective = false
    # 公共事件 ID 是有效的情况下,设置为有效标志
    effective |= skill.common_event_id > 0
    # 命中判定
    hit = skill.hit
    hit_result= (rand(100) < hit)
    if skill.atk_f > 10
      hit *= user.hit / 100
    end
    hit_result &= (rand(100) < hit)
    hit=90+[100*(user.int-self.int)/self.int,-65].max
    hit_result &= (rand(100) < hit)
    # 命中的情况下
    if hit_result == true
      # 计算威力
      power = skill.power + user.atk * skill.atk_f / 100
      if power > 0
        power*=[user.int.to_f/self.int.to_f,0.1].max
        power -= self.mdef/2
        power = [power, 1.1].max
      end
      self.damage=power.truncate
      # 属性修正
      self.damage *= elements_correct(skill.element_set)
      self.damage /= 100
      # 分散
      if self.damage.abs > 0
        amp = rand(1500)+9250.0+(user.int-self.int)*5
        self.damage *= amp/10000
      end
      self.damage=self.damage.truncate
      # 不确定的特技的情况下设置为有效标志
      effective |= hit < 100
    end
    # 命中的情况下
    if hit_result == true
      # 威力 0 以外的物理攻击的情况下
      if skill.power != 0 and skill.atk_f > 0
        # 状态冲击解除
        remove_states_shock
        # 设置有效标志
        effective = true
      end
      unless words[1].nil?
        if words[1].is_a?(Array)
          index=[words[1].size*self.damage/self.maxhp,words[1].size-1].min
          $defend_text=words[1][index]
        else
          $defend_text=words[1]
        end
      else
        $defend_text=""
      end
      # HP 的伤害减法运算
      last_hp = self.hp
      self.hp -= self.damage
      self.maxhp -= self.damage/2 if rand(100)<65 and self.is_a?(Game_Actor)
      effective |= self.hp != last_hp
      if self.is_a?(Game_Actor) and skill.id==123
        self.weapon_id=0
      end
      if self.is_a?(Game_Actor) and skill.id==112
        self.weapon_id=0 if rand(100)<50
      end
      # 状态变化
      @state_changed = false
      effective |= states_plus(skill.plus_state_set)
      effective |= states_minus(skill.minus_state_set)
      # 威力为 0 的场合
      if skill.power == 0
        # 伤害设置为空的字串
        self.damage = ""
        # 状态没有变化的情况下
        unless @state_changed
          # 伤害设置为 "Miss"
          self.damage = "Miss"
        end
      end
    # Miss 的情况下
    else
      effective=true
      unless words[2].nil?
        $defend_text=words[2]
      else
        $defend_text=""
      end
      # 伤害设置为 "Miss"
      self.damage = "Miss"
    end
    # 过程结束
    return effective
  end
  #--------------------------------------------------------------------------
  # ● 应用物品效果
  #     item : 物品
  #--------------------------------------------------------------------------
  def item_effect(item)
    # 清除会心一击标志
    self.critical = false
    # 物品的效果范围是 HP 1 以上的己方、自己的 HP 为 0、
    # 或者物品的效果范围是 HP 0 的己方、自己的 HP 为 1 以上的情况下
    if ((item.scope == 3 or item.scope == 4) and self.hp == 0) or
       ((item.scope == 5 or item.scope == 6) and self.hp >= 1)
      # 过程结束
      return false
    end
    # 清除有效标志
    effective = false
    # 公共事件 ID 是有效的情况下,设置为有效标志
    effective |= item.common_event_id > 0
    # 命中判定
    hit_result = (rand(100) < item.hit)
    # 不确定的特技的情况下设置为有效标志
    effective |= item.hit < 100
    # 命中的情况
    if hit_result == true
      # 计算回复量
      recover_hp = maxhp * item.recover_hp_rate / 100 + item.recover_hp
      recover_sp = maxsp * item.recover_sp_rate / 100 + item.recover_sp
      if recover_hp < 0
        recover_hp += self.pdef * item.pdef_f / 20
        recover_hp += self.mdef * item.mdef_f / 20
        recover_hp = [recover_hp, 0].min
      end
      # 属性修正
      recover_hp *= elements_correct(item.element_set)
      recover_hp /= 100
      recover_sp *= elements_correct(item.element_set)
      recover_sp /= 100
      # 分散
      if item.variance > 0 and recover_hp.abs > 0
        amp = [recover_hp.abs * item.variance / 100, 1].max
        recover_hp += rand(amp+1) + rand(amp+1) - amp
      end
      if item.variance > 0 and recover_sp.abs > 0
        amp = [recover_sp.abs * item.variance / 100, 1].max
        recover_sp += rand(amp+1) + rand(amp+1) - amp
      end
      # 回复量符号为负的情况下
      if recover_hp < 0
        # 防御修正
        if self.guarding?
          recover_hp /= 2
        end
      end
      # HP 回复量符号的反转、设置伤害值
      self.damage = -recover_hp
      # HP 以及 SP 的回复
      last_hp = self.hp
      last_sp = self.sp
      self.hp += recover_hp
      self.sp += recover_sp
      effective |= self.hp != last_hp
      effective |= self.sp != last_sp
      # 状态变化
      @state_changed = false
      effective |= states_plus(item.plus_state_set)
      effective |= states_minus(item.minus_state_set)
      # 能力上升值有效的情况下
      if item.parameter_type > 0 and item.parameter_points != 0
        # 能力值的分支
        case item.parameter_type
        when 1  # MaxHP
          @maxhp_plus += item.parameter_points
        when 2  # MaxSP
          @maxsp_plus += item.parameter_points
        when 3  # 力量
          @str_plus += item.parameter_points
        when 4  # 灵巧
          @dex_plus += item.parameter_points
        when 5  # 速度
          @agi_plus += item.parameter_points
        when 6  # 魔力
          @int_plus += item.parameter_points
        end
        # 设置有效标志
        effective = true
      end
      # HP 回复率与回复量为 0 的情况下
      if item.recover_hp_rate == 0 and item.recover_hp == 0
        # 设置伤害为空的字符串
        self.damage = ""
        # SP 回复率与回复量为 0、能力上升值无效的情况下
        if item.recover_sp_rate == 0 and item.recover_sp == 0 and
           (item.parameter_type == 0 or item.parameter_points == 0)
          # 状态没有变化的情况下
          unless @state_changed
            # 伤害设置为 "Miss"
            self.damage = "Miss"
          end
        end
      end
    # Miss 的情况下
    else
      # 伤害设置为 "Miss"
      self.damage = "Miss"
    end
    # 不在战斗中的情况下
    unless $game_temp.in_battle
      # 伤害设置为 nil
      self.damage = nil
    end
    # 过程结束
    return effective
  end
  #--------------------------------------------------------------------------
  # ● 应用连续伤害效果
  #--------------------------------------------------------------------------
  def slip_damage_effect
    # 设置伤害
    self.damage = self.maxhp / 20
    # 分散
    if self.damage.abs > 0
      amp = [self.damage.abs * 20 / 100, 1].max
      self.damage += rand(amp+1) + rand(amp+1) - amp
    end
    # HP 的伤害减法运算
    self.hp -= self.damage
    # 过程结束
    return true
  end
  #--------------------------------------------------------------------------
  # ● 属性修正计算
  #     element_set : 属性
  #--------------------------------------------------------------------------
  def elements_correct(element_set)
    # 無属性的情况
    if element_set == []
      # 返回 100
      return 100
    end
    # 在被赋予的属性中返回最弱的
    # ※过程 element_rate 是、本类以及继承的 Game_Actor
    #   和 Game_Enemy 类的定义
    weakest = -100
    for i in element_set
      weakest = [weakest, self.element_rate(i)].max
    end
    return weakest
  end
end