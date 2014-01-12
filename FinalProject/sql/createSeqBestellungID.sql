declare
  -- Start value for sequence.
  startBSTID number;
  -- SQL Statement.
  sql_stmt varchar2(600);
begin
  -- Paste the max value of BSTID from table BESTELLUNG into variable startBSTID.
  select max(BSTID) into startBSTID from BESTELLUNG;
  -- Increase the value startBSTID by 1.
  startBSTID := startBSTID + 1;
  -- SQL Statement to create the sequence for BESTELLUNG.BSTID
  sql_stmt := 'CREATE SEQUENCE seq_BESTELLUNG_BSTID ' ||
              '  START WITH ' || startBSTID || ' ' ||
              '  INCREMENT BY 1 ' ||
              '  MINVALUE ' || startBSTID || ' ' ||
              '  CACHE 10 ' || 
              '  NOCYCLE ' ||
              '  NOORDER';
  execute immediate sql_stmt;              
end;
/
