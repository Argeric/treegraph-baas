package org.treegraph.baas.common.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 * mybatis generator生成注释插件
 */
public class MyCommentGenerator extends DefaultCommentGenerator {

  private final boolean suppressAllComments;

  public MyCommentGenerator() {
    super();
    suppressAllComments = false;
  }


  public void addFieldComment(Field field, IntrospectedTable introspectedTable,
      IntrospectedColumn introspectedColumn) {
    if (suppressAllComments) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    field.addJavaDocLine("/**");
    sb.append(" * ");
    sb.append(introspectedColumn.getRemarks());
    field.addJavaDocLine(sb.toString().replace("\n", " "));
    field.addJavaDocLine(" */");
  }

  public void addFieldComment(Field field, IntrospectedTable introspectedTable) {

  }

  public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

  }

  public void addGetterComment(Method method, IntrospectedTable introspectedTable,
      IntrospectedColumn introspectedColumn) {

  }

  public void addSetterComment(Method method, IntrospectedTable introspectedTable,
      IntrospectedColumn introspectedColumn) {

  }

  public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable,
      boolean markAsDoNotDelete) {

  }

  public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
  }


}