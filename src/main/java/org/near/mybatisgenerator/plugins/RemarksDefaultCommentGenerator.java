package org.near.mybatisgenerator.plugins;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * Created by  lz on 2017-4-10.
 */
public class RemarksDefaultCommentGenerator extends DefaultCommentGenerator {

    private static final Logger LOGGER = Logger
        .getLogger(RemarksDefaultCommentGenerator.class.getName());

    private Properties          properties;
    private Properties          systemPro;
    private boolean             suppressDate;
    private boolean             suppressAllComments;
    private String              currentDateStr;
    private boolean             addRemarkComments;

    public RemarksDefaultCommentGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        addRemarkComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        //$NON-NLS-1$
        field.addJavaDocLine("/**");

        String remarks = introspectedColumn.getRemarks();
        //$NON-NLS-1$
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            field.addJavaDocLine(" * Database Column Remarks:");
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            //$NON-NLS-1$
            for (String remarkLine : remarkLines) {
                field.addJavaDocLine(" *" + remarkLine);
            }
        } //$NON-NLS-1$
          //$NON-NLS-1$
        field.addJavaDocLine("*");
        field.addJavaDocLine("*");
        //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append("* ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());

        field.addJavaDocLine(sb.toString());

        field.addJavaDocLine(StringUtility.stringHasValue(remarks) ? " * " + remarks : "");
        addJavadocTag(field, false);
        //$NON-NLS-1$
        field.addJavaDocLine(" */");
        LOGGER.log(Level.OFF, "sb:" + sb);
    }

    @Override
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else if (currentDateStr != null) {
            return currentDateStr;
        } else {
            return new Date().toString();
        }
    }
}
