declare
  -- Start value for sequence.
  startKID number;
  -- SQL Statement.
  sql_stmt varchar2(600);
begin
  -- Paste the max value of KID from table KUNDE into variable startID.
  select max(KID) into startKID from KUNDE;
  -- Increase the value startKID by 1.
  startKID := startKID + 1;
  -- SQL Statement to create the sequence for KUNDE.KID
  sql_stmt := 'CREATE SEQUENCE seq_Kunde_KID ' ||
              '  START WITH ' || startKID || ' ' ||
              '  INCREMENT BY 1 ' ||
              '  MINVALUE ' || startKID || ' ' ||
              '  CACHE 10 ' || 
              '  NOCYCLE ' ||
              '  NOORDER';
  execute immediate sql_stmt;              
end;
/
