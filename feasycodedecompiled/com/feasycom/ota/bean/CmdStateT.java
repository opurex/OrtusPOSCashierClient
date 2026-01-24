package com.feasycom.ota.bean;

import kotlin.Metadata;

@Metadata(
   mv = {1, 7, 1},
   k = 1,
   xi = 48,
   d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"},
   d2 = {"Lcom/feasycom/ota/bean/CmdStateT;", "", "(Ljava/lang/String;I)V", "CMD_PARSER_W4_HA", "CMD_PARSER_W4_HT", "CMD_PARSER_W4_TR", "CMD_PARSER_W4_TN", "CMD_PARSER_W4_PROCESS", "FeasyBlueLibrary_release"}
)
public enum CmdStateT {
   CMD_PARSER_W4_HA,
   CMD_PARSER_W4_HT,
   CMD_PARSER_W4_TR,
   CMD_PARSER_W4_TN,
   CMD_PARSER_W4_PROCESS;

   // $FF: synthetic method
   private static final CmdStateT[] $values() {
      return new CmdStateT[]{CMD_PARSER_W4_HA, CMD_PARSER_W4_HT, CMD_PARSER_W4_TR, CMD_PARSER_W4_TN, CMD_PARSER_W4_PROCESS};
   }
}
