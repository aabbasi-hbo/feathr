import React from "react";

import { Layout, Space } from "antd";
import dayjs from "dayjs";

import styles from "./index.module.less";

const { Footer } = Layout;

const FooterBar = () => {
  const generatedTime = dayjs(process.env.FEATHR_GENERATED_TIME)
    .utc()
    .format("YYYY-MM-DD HH:mm:DD UTC");

  return (
    <Footer className={styles.footer}>
      <Space size={32}>
        <span>Feathr UI Version: {process.env.FEATHR_VERSION}</span>
        <span>Feathr UI Build Generated at {generatedTime}</span>
      </Space>
    </Footer>
  );
};

export default FooterBar;
